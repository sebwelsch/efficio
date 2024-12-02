package keac4.efficio.controller;

import jakarta.servlet.http.HttpSession;
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

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/overview")
    public String showProjectsOverview(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("message", "You need to log in to access this page");
            return "redirect:/login";
        }
        model.addAttribute("projects", projectService.getProjectsByUserId(loggedInUser.getUserId()));
        return "userOverview";
    }

    @GetMapping("/project/add")
    public String showAddForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("message", "You need to log in to access this page");
            return "redirect:/login";
        }
        model.addAttribute("project", new Project());
        return "addProject";
    }

    @PostMapping("/project/add")
    public String addProject(@ModelAttribute Project project, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "You need to log in to add a project");
            return "redirect:/login";
        }

        // Add project and associate it with the logged-in user. Keyholder helps it connect to the person making it.
        projectService.addProject(project, loggedInUser.getUserId());

        redirectAttributes.addFlashAttribute("message", "Project added successfully");
        return "redirect:/overview";
    }

    @GetMapping("/project/{projectId}")
    public String showProjectPage(@PathVariable int projectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        // Check if there is a session
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "You need to log in to access this page");
            return "redirect:/login";
        }
        // If there is a session, check if user has access to the project
        boolean hasAccess = projectService.doesUserHaveAccess(projectId, loggedInUser.getUserId());
        if (!hasAccess) {
            redirectAttributes.addFlashAttribute("error", "You do not have access to this project. Ask someone with access to share it with you.");
            return "error/403";
        }

        Project project = projectService.getProjectById(projectId);
        model.addAttribute("project", project);
        List<Subproject> subprojects = projectService.getSubProjectsByProjectId(projectId);
        model.addAttribute("subprojects", subprojects);
        return "projectOverview";
    }


}