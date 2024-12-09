package keac4.efficio.service;

import keac4.efficio.model.Project;
import keac4.efficio.model.Subproject;
import keac4.efficio.model.Task;
import keac4.efficio.repository.ProjectRepository;
import keac4.efficio.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }


    public void saveNewTask(Task task) {
        task.setSubprojectId(task.getSubprojectId());
        taskRepository.saveNewTask(task);

        Subproject subproject = projectRepository.getSubprojectById(task.getSubprojectId());
        int newExpectedTimeSubproject = subproject.getExpectedTime() + task.getExpectedTime();
        projectRepository.updateExpectedTimeSubproject(subproject.getSubprojectId(), newExpectedTimeSubproject);

        Project project = projectRepository.getProjectById(subproject.getProjectId());
        int newExpectedTimeProject = project.getExpectedTime() + task.getExpectedTime();
        projectRepository.updateExpectedTimeProject(project.getProjectId(), newExpectedTimeProject);
    }


    public boolean doesUserHaveAccess(int projectId, int userId) {
        return false;
    }

}
