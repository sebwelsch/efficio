package keac4.efficio.controller;

import jakarta.servlet.http.HttpSession;
import keac4.efficio.model.Project;
import keac4.efficio.model.User;
import keac4.efficio.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/project/{projectId}")
    public String showProjectPage(@PathVariable int projectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        // Check if there is a session
        if (sessionUser == null) {
            redirectAttributes.addFlashAttribute("error", "You need to log in to access this page");
            return "redirect:/login";
        }
        // If there is a session, check if user has access to the project
        boolean hasAccess = projectService.doesUserHaveAccess(projectId, sessionUser.getUserId());
        if (!hasAccess) {
            redirectAttributes.addFlashAttribute("error", "You do not have access to this project. Ask someone with access to share it with you.");
            return "error/403";
        }

        Project project = projectService.getProjectById(projectId);
        model.addAttribute("project", project);

        return "projectOverview";
    }

}
