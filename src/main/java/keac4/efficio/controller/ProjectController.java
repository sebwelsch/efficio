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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping()
public class ProjectController {

    private final ProjectService projectService;
    private final ValidateAccess validateAccess;
    private final TaskService taskService;

    @Autowired
    public ProjectController(ProjectService projectService, ValidateAccess validateAccess, TaskService taskService) {
        this.projectService = projectService;
        this.validateAccess = validateAccess;
        this.taskService = taskService;
    }

    @GetMapping("/overview")
    public String showUserOverview(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, null);
        if (userAccess != null) {
            return userAccess;
        }

        User userSession = (User) session.getAttribute("userSession");
        model.addAttribute("projects", projectService.getProjectsByUserId(userSession.getUserId()));
        return "userOverview";
    }

    /**
     * Show the view for creating a new project
     *
     * @param model              used to add attributes for the view
     * @param session            to check if there is a HTTPSession
     * @param redirectAttributes add flash attributes to a redirect
     * @return view to create a new project
     */
    @GetMapping("/project/create")
    public String showCreateProjectPage(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, null);
        if (userAccess != null) {
            return userAccess;
        }

        model.addAttribute("project", new Project());
        return "createProject";
    }

    @PostMapping("/project/create")
    public String createProject(@ModelAttribute Project project, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        String userAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, null);
        if (userAccess != null) {
            return userAccess;
        }

        // Create project and associate it with the logged-in user. Keyholder helps it connect to the person making it.
        User userSession = (User) session.getAttribute("userSession");
        projectService.createProject(project, userSession.getUserId());

        redirectAttributes.addFlashAttribute("success", "Project added successfully");
        return "redirect:/overview";
    }

    /**
     * @param projectId          used to access a specific project and get its subprojects
     * @param model              used to add attributes for the view
     * @param session            to check if there is a HTTPSession
     * @param redirectAttributes add flash attributes to a redirect
     * @return view for the projects overview
     */
    @GetMapping("/project/{projectId}")
    public String showProjectPage(@PathVariable int projectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, projectId);
        if (userAccess != null) {
            return userAccess;
        }


        Project project = projectService.getProjectById(projectId);

        double hoursPerDay = projectService.calculateHoursPerDay(project.getStartDate(), project.getDeadline(), project.getExpectedTime());
        model.addAttribute("hoursPerDay", hoursPerDay);

        model.addAttribute("project", project);

        User userSession = (User) session.getAttribute("userSession");
        model.addAttribute("projects", projectService.getProjectsByUserId(userSession.getUserId()));

        List<Subproject> subprojects = projectService.getAllSubprojectsByProjectId(projectId);
        model.addAttribute("subprojects", subprojects);

        return "projectOverview";
    }

    @PostMapping("/project/{projectId}/share")
    public String shareProject(@PathVariable int projectId, @RequestParam String username, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        String userAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, projectId);
        if (userAccess != null) {
            return userAccess;
        }

        boolean sharedAccess = projectService.shareProject(projectId, username);
        if (!sharedAccess) {
            redirectAttributes.addFlashAttribute("error", "Username does not exist or project is already shared with this user");
        } else {
            redirectAttributes.addFlashAttribute("success", "Project was shared with user " + username);
        }
        return "redirect:/project/" + projectId;
    }

    @GetMapping("/project/{projectId}/update")
    public String showUpdateProjectPage(@PathVariable int projectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, projectId);
        if (userAccess != null) {
            return userAccess;
        }

        Project project = projectService.getProjectById(projectId);
        model.addAttribute("project", project);
        return "updateProject";
    }

    @PostMapping("/project/{projectId}/update")
    public String updateProject(@PathVariable int projectId, @ModelAttribute Project project, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        String userAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, projectId);
        if (userAccess != null) {
            return userAccess;
        }

        projectService.updateProject(project);
        redirectAttributes.addFlashAttribute("success", "Successfully updated project");
        return "redirect:/project/" + projectId;
    }

    @PostMapping("/project/delete/{projectId}")
    public String deleteProject(@PathVariable int projectId, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        String userHasAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, projectId);
        if (userHasAccess != null) {
            return userHasAccess;
        }

        projectService.deleteProjectById(projectId);
        redirectAttributes.addFlashAttribute("success", "Project deleted successfully");
        return "redirect:/overview";
    }

    @GetMapping("/project/{projectId}/subproject/create")
    public String showCreateSubprojectPage(@PathVariable int projectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, projectId);
        if (userAccess != null) {
            return userAccess;
        }

        model.addAttribute("project", projectService.getProjectById(projectId));
        model.addAttribute("subproject", new Subproject());
        return "createSubproject";
    }

    @PostMapping("/project/{projectId}/subproject/create")
    public String createSubproject(@ModelAttribute Subproject subproject, @PathVariable int projectId, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        String userAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, projectId);
        if (userAccess != null) {
            return userAccess;
        }

        projectService.createSubproject(subproject, projectId);

        return "redirect:/project/" + projectId;
    }

    @GetMapping("/project/{projectId}/subproject/{subprojectId}")
    public String showSubprojectPage(@PathVariable int projectId, @PathVariable int subprojectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
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

        double hoursPerDay = projectService.calculateHoursPerDay(subproject.getStartDate(), subproject.getDeadline(), subproject.getExpectedTime());
        model.addAttribute("hoursPerDay", hoursPerDay);
        return "subprojectOverview";
    }

    @GetMapping("/project/{projectId}/subproject/{subprojectId}/update")
    public String showUpdateSubprojectPage(@PathVariable int projectId, @PathVariable int subprojectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, projectId);
        if (userAccess != null) {
            return userAccess;
        }

        Subproject subproject = projectService.getSubprojectById(subprojectId);
        model.addAttribute("subproject", subproject);
        return "updateSubproject";
    }

    @PostMapping("/project/{projectId}/subproject/{subprojectId}/update")
    public String updateSubproject(@PathVariable int projectId, @PathVariable int subprojectId, @ModelAttribute Subproject subproject, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        String userAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, projectId);
        if (userAccess != null) {
            return userAccess;
        }

        projectService.updateSubproject(subproject);
        redirectAttributes.addFlashAttribute("success", "Successfully updated project");
        String redirectLink = "redirect:/project/" + projectId + "/subproject/" + subprojectId;
        return redirectLink;
    }

    @PostMapping("/project/{projectId}/subproject/{subprojectId}/delete")
    public String deleteSubproject(@PathVariable int projectId, @PathVariable int subprojectId, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        String userHasAccess = validateAccess.validateUserAccess(session, model, redirectAttributes, projectId);
        if (userHasAccess != null) {
            return userHasAccess;
        }

        projectService.deleteSubprojectById(subprojectId);
        redirectAttributes.addFlashAttribute("success", "Project deleted successfully");
        return "redirect:/project/" + projectId;
    }
}