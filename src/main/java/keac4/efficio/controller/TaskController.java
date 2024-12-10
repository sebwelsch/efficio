package keac4.efficio.controller;

import jakarta.servlet.http.HttpSession;
import keac4.efficio.component.ValidateAccess;
import keac4.efficio.model.Project;
import keac4.efficio.model.Task;
import keac4.efficio.repository.TaskRepository;
import keac4.efficio.service.ProjectService;
import keac4.efficio.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TaskController {

    private final ProjectService projectService;
    private final ValidateAccess validateAccess;
    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @Autowired
    public TaskController(ProjectService projectService, ValidateAccess validateAccess, TaskRepository taskRepository, TaskService taskService) {
        this.projectService = projectService;
        this.validateAccess = validateAccess;
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

    @GetMapping("/project/{projectId}/subproject/{subprojectId}/tasks/add")
    public String showAddTaskForm(@PathVariable int projectId, @PathVariable int subprojectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, redirectAttributes, null);
        if (userAccess != null) {
            return userAccess;
        }

        model.addAttribute("task", new Task());
        model.addAttribute("projectId", projectId);
        model.addAttribute("subprojectId", subprojectId);
        Project project = projectService.getProjectById(projectId);
        model.addAttribute("project", project);
        return "addTask";
    }

    @PostMapping("/project/{projectId}/subproject/{subprojectId}/tasks/add")
    public String addTask(@PathVariable int subprojectId, @PathVariable int projectId, @ModelAttribute Task task, HttpSession session, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, redirectAttributes, null);
        if (userAccess != null) {
            return userAccess;
        }

        Project.setProjectID(projectId);
        task.setSubprojectId(subprojectId);
        taskService.saveNewTask(task);

        redirectAttributes.addFlashAttribute("success", "Task added successfully!");
        String redirectLink = "redirect:/project/" + projectId + "/subproject/" + subprojectId;
        return redirectLink;
    }

    @GetMapping("/project/{projectId}/subproject/{subprojectId}/tasks/{taskId}/edit")
    public String showEditTaskForm(@PathVariable int projectId, @PathVariable int subprojectId, @PathVariable int taskId, Model model, RedirectAttributes redirectAttributes) {
        Task existingTask = taskService.getTaskById(taskId);

        if (existingTask == null) {
            redirectAttributes.addFlashAttribute("error", "Task not found");
            String redirectLink = "redirect:/project/" + projectId + "/subproject/" + subprojectId;
            return redirectLink;

        }

        model.addAttribute("task", existingTask);
        model.addAttribute("taskId", taskId);
        model.addAttribute("projectId", projectId);
        model.addAttribute("subprojectId", subprojectId);

        return "editTask";
    }

    @PostMapping("/project/{projectId}/subproject/{subprojectId}/tasks/{taskId}/edit")
    public String updateTask(@PathVariable int projectId, @PathVariable int subprojectId, @PathVariable int taskId, @ModelAttribute Task task, RedirectAttributes redirectAttributes) {
        task.setTaskId(taskId);
        taskService.updateTask(task);

        redirectAttributes.addFlashAttribute("success", "Task updated successfully!");
        String redirectLink = "redirect:/project/" + projectId + "/subproject/" + subprojectId;
        return redirectLink;

    }

    @PostMapping("/{projectId}/subprojects/{subprojectId}/tasks/{taskId}/delete")
    public String deleteTask(@PathVariable int projectId, @PathVariable int subprojectId, @PathVariable int taskId, RedirectAttributes redirectAttributes, HttpSession session) {
        String userHasAccess = validateAccess.validateUserAccess(session, redirectAttributes, projectId);
        if (userHasAccess != null) {
            return userHasAccess;
        }
        String result = taskService.deleteTask(taskId, subprojectId);
        if (result.equals("Task deleted successfully.")) {
            redirectAttributes.addFlashAttribute("success", result);
        } else {
            redirectAttributes.addFlashAttribute("error", result);
        }
        return "redirect:/subProjectOverview/";
    }
}
