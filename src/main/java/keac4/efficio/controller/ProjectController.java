package keac4.efficio.controller;

import keac4.efficio.model.Project;
import keac4.efficio.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/project/overview")
    public String showProjectsOverview(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "projects-overview";
    }

    @GetMapping("/project/add")
    public String showAddForm(Model model) {
        model.addAttribute("project", new Project());
        return "add-project";
    }
    @PostMapping("/project/add")
    public String addProject(@ModelAttribute Project project, Model model) {
        projectService.addProject(project);
        model.addAttribute("message", "project added successfully");
        return "redirect:/project/overview";

    }
}