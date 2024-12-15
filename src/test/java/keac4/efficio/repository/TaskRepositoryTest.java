package keac4.efficio.repository;

import keac4.efficio.model.Project;
import keac4.efficio.model.Subproject;
import keac4.efficio.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = "spring.profiles.active=test")
class TaskRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    private Project createdProject;
    private Subproject createdSubproject;
    private Task createdTask;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        // Creating a project that can be used in the tests
        Project project = new Project();
        project.setName("Test project");
        project.setDescription("This is a project for test purposes");
        project.setStartDate("2024-12-01");
        project.setDeadline("2024-12-24");

        int projectId = projectRepository.createProject(project);
        createdProject = projectRepository.getProjectById(projectId);

        // Creating a subproject that can be used in the tests
        Subproject subproject = new Subproject();
        subproject.setName("Test subproject");
        subproject.setDescription("This is a subproject for test purposes");
        subproject.setStartDate("2024-12-05");
        subproject.setDeadline("2024-12-22");

        projectRepository.createSubproject(subproject, createdProject.getProjectId());
        List<Subproject> subprojectlist = projectRepository.getAllSubprojectsByProjectId(createdProject.getProjectId());
        createdSubproject = subprojectlist.getFirst();

        // Creating a task for test use
        Task task = new Task();
        task.setName("Test task");
        task.setDescription("This is a task for test purposes");
        task.setExpectedTime(5);
        task.setSubprojectId(createdSubproject.getSubprojectId());


        taskRepository.saveNewTask(task);
        List<Task> savedTasks = taskRepository.getAllTasksBySubprojectId(task.getSubprojectId());
        createdTask = savedTasks.getFirst();
    }

    @Test
    void saveNewTask() {
        Task task = new Task();
        task.setName("Test task 2");
        task.setDescription("This is a task for test purposes that is different from the in in setUp()");
        task.setExpectedTime(11);
        task.setSubprojectId(createdSubproject.getSubprojectId());

        taskRepository.saveNewTask(task);
        List<Task> savedTasks = taskRepository.getAllTasksBySubprojectId(task.getSubprojectId());

        assertAll("Test if all task variables are the same",
                () -> assertEquals(task.getName(), savedTasks.get(1).getName()),
                () -> assertEquals(task.getDescription(), savedTasks.get(1).getDescription()),
                () -> assertEquals(task.getExpectedTime(), savedTasks.get(1).getExpectedTime())
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