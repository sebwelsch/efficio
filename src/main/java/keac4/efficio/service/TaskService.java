package keac4.efficio.service;

import keac4.efficio.model.Project;
import keac4.efficio.model.Subproject;
import keac4.efficio.model.Task;
import keac4.efficio.repository.ProjectRepository;
import keac4.efficio.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        taskRepository.saveNewTask(task);

        //Add time to subproject and project
        Subproject subproject = projectRepository.getSubprojectById(task.getSubprojectId());
        int newExpectedTimeSubproject = subproject.getExpectedTime() + task.getExpectedTime();
        projectRepository.updateExpectedTimeSubproject(subproject.getSubprojectId(), newExpectedTimeSubproject);

        Project project = projectRepository.getProjectById(subproject.getProjectId());
        int newExpectedTimeProject = project.getExpectedTime() + task.getExpectedTime();
        projectRepository.updateExpectedTimeProject(project.getProjectId(), newExpectedTimeProject);
    }

    public Task getTaskById(int taskId) {
        return taskRepository.findTaskById(taskId);
    }

    public List<Task> getAllTasksBySubprojectId(int subprojectId) {
        return taskRepository.getAllTasksBySubprojectId(subprojectId);
    }

    public String deleteTask(int taskId, int subprojectId) {
        Task task = taskRepository.findTaskById(taskId);

        int rowsAffected = taskRepository.deleteTaskById(taskId);

        if (rowsAffected > 0) {
            //Remove time from subproject and project
            Subproject subproject = projectRepository.getSubprojectById(task.getSubprojectId());
            int newExpectedTimeSubproject = subproject.getExpectedTime() - task.getExpectedTime();
            projectRepository.updateExpectedTimeSubproject(subproject.getSubprojectId(), newExpectedTimeSubproject);

            Project project = projectRepository.getProjectById(subproject.getProjectId());
            int newExpectedTimeProject = project.getExpectedTime() - task.getExpectedTime();
            projectRepository.updateExpectedTimeProject(project.getProjectId(), newExpectedTimeProject);

            return "Task deleted successfully.";
        } else {
            return "Task not found for the given subproject.";
        }
    }
}
