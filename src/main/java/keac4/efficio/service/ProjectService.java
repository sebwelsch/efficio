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
import java.time.format.DateTimeParseException;
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

    public boolean shareProject(int projectId, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        return projectRepository.linkProjectToUser(projectId, user.getUserId());
    }

    public void createSubproject(Subproject subproject, int projectId) {
        projectRepository.createSubproject(subproject, projectId);
    }

    public double calculateHoursPerDay(String projectStartDate, String projectDeadline, int expectedTime) {
        if (projectStartDate == null || projectStartDate.isEmpty() ||
                projectDeadline == null || projectDeadline.isEmpty()) {
            return 0;
        }

        LocalDate startDate = LocalDate.parse(projectStartDate);
        LocalDate deadline = LocalDate.parse(projectDeadline);

        double workdays = 0;
        while (!startDate.isAfter(deadline)) {
            if (startDate.getDayOfWeek() != DayOfWeek.SATURDAY && startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workdays++;
            }
            startDate = startDate.plusDays(1);
        }

        if (workdays == 0) return 0;

        double hoursPerDay = (double) expectedTime / workdays;
        return Math.round(hoursPerDay * 100.0) / 100.0; // Round to two decimal places
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

    public void deleteProjectById(int projectId) {
        projectRepository.deleteProjectById(projectId);
    }

    public void deleteSubprojectById(int subprojectId) {
        Subproject subproject = projectRepository.getSubprojectById(subprojectId);
        //Remove time from project
        Project project = projectRepository.getProjectById(subproject.getProjectId());
        int newExpectedTimeProject = project.getExpectedTime() - subproject.getExpectedTime();
        projectRepository.updateExpectedTimeProject(project.getProjectId(), newExpectedTimeProject);

        projectRepository.deleteSubprojectById(subprojectId);
    }
}
