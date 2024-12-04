package keac4.efficio.service;

import keac4.efficio.model.Task;
import keac4.efficio.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public void saveNewTask(Task task) {
        task.setSubprojectId(task.getSubprojectId());
        taskRepository.saveNewTask(task);
    }


    public boolean doesUserHaveAccess(int projectId, int userId) {
        return false;
    }

}
