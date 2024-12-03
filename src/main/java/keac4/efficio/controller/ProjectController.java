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
    public String showUserOverview(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, redirectAttributes, null);
        if (userAccess != null) {
            return userAccess;
        }

        User userSession = (User) session.getAttribute("userSession");
        model.addAttribute("projects", projectService.getProjectsByUserId(userSession.getUserId()));
        return "userOverview";
    }

    /**
     * Show the view for creating a new project
     * @param model used to add attributes for the view
     * @param session to check if there is a HTTPSession
     * @param redirectAttributes add flash attributes to a redirect
     * @return view to create a new project
     */
    @GetMapping("/project/create")
    public String showCreateProjectPage(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, redirectAttributes, null);
        if (userAccess != null) {
            return userAccess;
        }

        model.addAttribute("project", new Project());
        return "createProject";
    }

    @PostMapping("/project/create")
    public String createProject(@ModelAttribute Project project, HttpSession session, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, redirectAttributes, null);
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
     *
     * @param projectId used to access a specific project and get its subprojects
     * @param model used to add attributes for the view
     * @param session to check if there is a HTTPSession
     * @param redirectAttributes add flash attributes to a redirect
     * @return view for the projects overview
     */
    @GetMapping("/project/{projectId}")
    public String showProjectPage(@PathVariable int projectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, redirectAttributes, projectId);
//        if (userAccess != null) {
//            return userAccess;
//        }

        Project project = projectService.getProjectById(projectId);
        model.addAttribute("project", project);
        List<Subproject> subprojects = projectService.getAllSubprojectsByProjectId(projectId);
        model.addAttribute("subprojects", subprojects);
        return "projectOverview";
    }

    @GetMapping("/project/{projectId}/subproject/create")
    public String showCreateSubprojectPage(@PathVariable int projectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, redirectAttributes, projectId);
        if (userAccess != null) {
            return userAccess;
        }

        model.addAttribute("projectId", projectId);
        model.addAttribute("subproject", new Subproject());
        return "createSubproject";
    }

    @PostMapping("/project/{projectId}/subproject/create")
    public String createSubproject(@ModelAttribute Subproject subproject, @PathVariable int projectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, redirectAttributes, projectId);
        if (userAccess != null) {
            return userAccess;
        }

        projectService.createSubproject(subproject, projectId);

        return "redirect:/project/" + projectId;
    }

    @GetMapping("/project/{projectId}/subproject/{subprojectId}")
    public String showSubprojectPage(@PathVariable int projectId, @PathVariable int subprojectId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String userAccess = validateAccess.validateUserAccess(session, redirectAttributes, projectId);
        if (userAccess != null) {
            return userAccess;
        }

        Subproject subproject = projectService.getSubprojectById(subprojectId);
        model.addAttribute("subproject", subproject);
        return "projectOverview";
    }
}