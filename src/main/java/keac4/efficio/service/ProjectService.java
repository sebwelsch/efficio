package keac4.efficio.service;

import keac4.efficio.model.Project;
import keac4.efficio.model.Subproject;
import keac4.efficio.model.User;
import keac4.efficio.repository.ProjectRepository;
import keac4.efficio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public void createProject(Project project, int userId) {
        int projectId = projectRepository.createProject(project);
        // Links the project to the user using the keyholder method.
        projectRepository.linkProjectToUser(projectId, userId);
    }

    public void shareProject(int projectId, String username) {
        User user = userRepository.findByUsername(username);
        projectRepository.linkProjectToUser(projectId, user.getUserId());
    }

    public void createSubproject(Subproject subproject, int projectId) {
        projectRepository.createSubproject(subproject, projectId);
    }

    public double calculateHoursPerDay(Project project) {
        LocalDate startDate = LocalDate.parse(project.getStartDate());
        LocalDate deadLine = LocalDate.parse(project.getDeadline());

        int workdays = 0;
        while(!startDate.isAfter(deadLine)) {
            if(startDate.getDayOfWeek() != DayOfWeek.SATURDAY && startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workdays++;
            }
            startDate = startDate.plusDays(1);
        }
        if(workdays == 0) return 0;
        return (double) project.getExpectedTime() / workdays;


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
