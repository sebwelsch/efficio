package keac4.efficio.service;

import keac4.efficio.model.Project;
import keac4.efficio.repository.ProjectRepository;
import keac4.efficio.model.Subproject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void addProject(Project newProject) {
        projectRepository.addProject(newProject);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public boolean doesUserHaveAccess(int projectId, int userId) {
        return projectRepository.doesUserHaveAccess(projectId, userId);
    }

    public Project getProjectById(int projectId) {
        return projectRepository.getProjectById(projectId);
    }

    public List<Subproject> getSubProjectsByProjectId(int projectId) {
        return projectRepository.getSubProjectsByProjectId(projectId);
    }


}
