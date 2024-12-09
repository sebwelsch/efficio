package keac4.efficio.service;

import keac4.efficio.model.Project;
import keac4.efficio.model.Subproject;
import keac4.efficio.repository.ProjectRepository;
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

    public void createProject(Project project, int userId) {
        int projectId = projectRepository.createProject(project);
        // Links the project to the user using the keyholder method.
        projectRepository.linkProjectToUser(projectId, userId);
    }

    public void createSubproject(Subproject subproject, int projectId) {
        projectRepository.createSubproject(subproject, projectId);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(int projectId) {
        return projectRepository.getProjectById(projectId);
    }

    public Subproject getSubprojectById(int subprojectId) {
        return projectRepository.getSubprojectById(subprojectId);
    }

    public void updateProject(Project project) {
        projectRepository.updateProject(project);
    }

    public void updateSubproject(Subproject subproject) {
        projectRepository.updateSubproject(subproject);
    }

    public List<Subproject> getAllSubprojectsByProjectId(int projectId) {
        return projectRepository.getAllSubprojectsByProjectId(projectId);
    }

    public List<Project> getProjectsByUserId(int userId) {
        return projectRepository.findByUserID(userId);
    }

    public void deleteProject(int projectId) {
        projectRepository.deleteById(projectId);
    }

}
