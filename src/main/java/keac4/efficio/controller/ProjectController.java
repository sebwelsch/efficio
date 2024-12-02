package keac4.efficio.controller;

import jakarta.servlet.http.HttpSession;
import keac4.efficio.component.ValidateAccess;
import keac4.efficio.model.Project;
import keac4.efficio.model.Subproject;
import keac4.efficio.model.User;
import keac4.efficio.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping()
public class ProjectController {

    private final ProjectService projectService;
    private final ValidateAccess validateAccess;

    @Autowired
    public ProjectController(ProjectService projectService, ValidateAccess validateAccess) {
        this.projectService = projectService;
        this.validateAccess = validateAccess;
    }

    @GetMapping("/overview")
    public String showProjectsOverview(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        // Using the component ValidateAccess to check if the user is allowed to access this endpoint
        String userHasAccess = validateAccess.validateUserAccess(session, redirectAttributes, null);
        if (userHasAccess != null) {
            return userHasAccess;
        }

        User userSession = (User) session.getAttribute("userSession");
        model.addAttribute("projects", projectService.getProjectsByUserId(userSession.getUserId()));
        return "userOverview";
    }

    @GetMapping("/project/create")
    public String showAddForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        // Using the component ValidateAccess to check if the user is allowed to access this endpoint
        String userHasAccess = validateAccess.validateUserAccess(session, redirectAttributes, null);
        if (userHasAccess != null) {
            return userHasAccess;
        }

        model.addAttribute("project", new Project());
        return "createProject";
    }

    @PostMapping("/project/create")
    public String createProject(@ModelAttribute Project project, HttpSession session, RedirectAttributes redirectAttributes) {
        // Using the component ValidateAccess to check if the user is allowed to access this endpoint
        String userHasAccess = validateAccess.validateUserAccess(session, redirectAttributes, null);
        if (userHasAccess != null) {
            return userHasAccess;
        }

        // Create project and associate it with the logged-in user. Keyholder helps it connect to the person making it.
        User userSession = (User) session.getAttribute("userSession");
        projectService.createProject(project, userSession.getUserId());

        redirectAttributes.addFlashAttribute("error", "Project added successfully");
        return "redirect:/overview";
    }

    @GetMapping("/project/{projectId}")
    public String showProjectPage(@PathVariable int projectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        // Using the component ValidateAccess to check if the user is allowed to access this endpoint
        String userHasAccess = validateAccess.validateUserAccess(session, redirectAttributes, projectId);
        if (userHasAccess != null) {
            return userHasAccess;
        }

        Project project = projectService.getProjectById(projectId);
        model.addAttribute("project", project);
        List<Subproject> subprojects = projectService.getSubProjectsByProjectId(projectId);
        model.addAttribute("subprojects", subprojects);
        return "projectOverview";
    }

    @GetMapping("/project/{projectId}/subproject/{subprojectId}")
    public String showSubprojectPage(@PathVariable int projectId, @PathVariable int subprojectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        // Using the component ValidateAccess to check if the user is allowed to access this endpoint
        String userHasAccess = validateAccess.validateUserAccess(session, redirectAttributes, projectId);
        if (userHasAccess != null) {
            return userHasAccess;
        }

        return "projectOverview";
    }
}