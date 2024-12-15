package keac4.efficio.controller;

import jakarta.servlet.http.HttpSession;
import keac4.efficio.component.ValidateAccess;
import keac4.efficio.model.Project;
import keac4.efficio.model.Subproject;
import keac4.efficio.model.Task;
import keac4.efficio.model.User;
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

import java.util.List;

@Controller
public class TaskController {

    private final ProjectService projectService;
    private final ValidateAccess validateAccess;
    private final TaskService taskService;

    @Autowired
    public TaskController(ProjectService projectService, ValidateAccess validateAccess, TaskService taskService) {
        this.projectService = projectService;
        this.validateAccess = validateAccess;
        this.taskService = taskService;
    }

    @GetMapping("/project/{projectId}/subproject/{subprojectId}/tasks/add")
    public String showAddTaskForm(@PathVariable int projectId, @PathVariable int subprojectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, projectId);
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
    public String addTask(@PathVariable int subprojectId, @PathVariable int projectId, @ModelAttribute Task task, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        String userAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, projectId);
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

    @GetMapping("/project/{projectId}/subproject/{subprojectId}/tasks/{taskId}")
    public String showTaskOverviewPage(@PathVariable int projectId, @PathVariable int subprojectId, @PathVariable int taskId, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, projectId);
        if (userAccess != null) {
            return userAccess;
        }

        // This is used for the side-nav to show the users projects
        User userSession = (User) session.getAttribute("userSession");
        model.addAttribute("projects", projectService.getProjectsByUserId(userSession.getUserId()));

        Subproject subproject = projectService.getSubprojectById(subprojectId);
        model.addAttribute("subproject", subproject);

        List<Task> tasks = taskService.getAllTasksBySubprojectId(subprojectId);
        model.addAttribute("tasks", tasks);

        Task task = taskService.getTaskById(taskId);
        model.addAttribute("task", task);
        return "taskOverview";
    }

    @PostMapping("/project/{projectId}/subproject/{subprojectId}/tasks/{taskId}/delete")
    public String deleteTask(@PathVariable int projectId, @PathVariable int subprojectId, @PathVariable int taskId, Model model, RedirectAttributes redirectAttributes, HttpSession session) {
        String userHasAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, projectId);
        if (userHasAccess != null) {
            return userHasAccess;
        }

        String result = taskService.deleteTask(taskId, subprojectId);
        if (result.equals("Task deleted successfully.")) {
            redirectAttributes.addFlashAttribute("success", result);
        } else {
            redirectAttributes.addFlashAttribute("error", result);
        }

        String redirectLink = "redirect:/project/" + projectId + "/subproject/" + subprojectId;
        return redirectLink;
    }
}
