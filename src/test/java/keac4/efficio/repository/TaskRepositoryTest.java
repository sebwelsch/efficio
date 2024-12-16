package keac4.efficio.repository;

import keac4.efficio.model.Project;
import keac4.efficio.model.Subproject;
import keac4.efficio.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active=test")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    private Project createdProject;
    private Subproject createdSubproject;
    private Task createdTask;

    @Autowired
    private ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        createdProject = createTestProject();
        createdSubproject = createTestSubproject();
        createdTask = createTestTask();
    }

    private Project createTestProject() {
        Project project = new Project();
        project.setName("Test project");
        project.setDescription("This is a project for test purposes");
        project.setStartDate("2024-12-01");
        project.setDeadline("2024-12-24");
        int projectId = projectRepository.createProject(project);
        return projectRepository.getProjectById(projectId);
    }

    private Subproject createTestSubproject() {
        Subproject subproject = new Subproject();
        subproject.setName("Test subproject");
        subproject.setDescription("This is a subproject for test purposes");
        subproject.setStartDate("2024-12-05");
        subproject.setDeadline("2024-12-22");
        projectRepository.createSubproject(subproject, createdProject.getProjectId());
        return projectRepository.getAllSubprojectsByProjectId(createdProject.getProjectId()).getFirst();
    }

    private Task createTestTask() {
        Task task = new Task();
        task.setName("Test task");
        task.setDescription("This is a task for test purposes");
        task.setExpectedTime(5);
        task.setSubprojectId(1);

        taskRepository.saveNewTask(task);
        return taskRepository.getAllTasksBySubprojectId(task.getSubprojectId()).getFirst();
    }

    @Test
    void saveNewTask() {
        Task newTask = new Task();
        newTask.setName("Test task 2");
        newTask.setDescription("This is a task for test purposes that is different from the one in setUp()");
        newTask.setExpectedTime(11);
        newTask.setSubprojectId(1);

        taskRepository.saveNewTask(newTask);
        Task savedTask = taskRepository.getAllTasksBySubprojectId(newTask.getSubprojectId()).get(1);

        assertAll("Test if all task variables are the same",
                () -> assertEquals(newTask.getName(), savedTask.getName()),
                () -> assertEquals(newTask.getDescription(), savedTask.getDescription()),
                () -> assertEquals(newTask.getExpectedTime(), savedTask.getExpectedTime())
        );
    }

    @Test
    void deleteTaskById() {
        int taskId = createdTask.getTaskId();
        taskRepository.deleteTaskById(taskId);

        Task task = taskRepository.findTaskById(taskId);
        assertNull(task);
    }
}
