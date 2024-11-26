package keac4.efficio.service;

import keac4.efficio.model.Project;
import keac4.efficio.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void addProject(Project newProject) {
        projectRepository.addProject(newProject);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }


}
