package keac4.efficio.service;

import keac4.efficio.model.Project;
import keac4.efficio.model.Subproject;
import keac4.efficio.model.Task;
import keac4.efficio.repository.ProjectRepository;
import keac4.efficio.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.profiles.active=test")
class TaskServiceTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectRepository projectRepository;

    private Project createdProject;
    private Subproject createdSubproject;

    @BeforeEach
    void setUp() {
        createdProject = createTestProject();
        createdSubproject = createTestSubproject();
    }

    private Project createTestProject() {
        Project project = new Project();
        project.setName("Test project");
        project.setDescription("This is a project for test purposes");
        project.setStartDate("2024-12-01");
        project.setDeadline("2024-12-24");
        int projectId = projectRepository.createProject(project);
        return projectService.getProjectById(projectId);
    }

    private Subproject createTestSubproject() {
        Subproject subproject = new Subproject();
        subproject.setName("Test subproject 2");
        subproject.setDescription("This is a subproject for test purposes and is different from the one in setUp()");
        subproject.setStartDate("2024-12-12");
        subproject.setDeadline("2024-12-20");

        projectService.createSubproject(subproject, createdProject.getProjectId());
        return projectService.getAllSubprojectsByProjectId(createdProject.getProjectId()).getFirst();
    }

    @Test
    void saveNewTask() {
        Task task = new Task();
        task.setName("Test task");
        task.setDescription("Description of task");
        task.setExpectedTime(11);
        task.setSubprojectId(createdSubproject.getSubprojectId());

        taskService.saveNewTask(task);
        Task savedTask = taskService.getAllTasksBySubprojectId(createdSubproject.getSubprojectId()).getFirst();
        assertAll("Test if all task variables are the same",
                () -> assertEquals(task.getName(), savedTask.getName()),
                () -> assertEquals(task.getDescription(), savedTask.getDescription()),
                () -> assertEquals(task.getExpectedTime(), savedTask.getExpectedTime())
        );

        // Checking that expected time is updated for subproject and project
        int projectTime = projectService.getProjectById(createdProject.getProjectId()).getExpectedTime();
        int subprojectTime = projectService.getSubprojectById(createdSubproject.getSubprojectId()).getExpectedTime();
        assertEquals(task.getExpectedTime(), subprojectTime);
        assertEquals(task.getExpectedTime(), projectTime);
    }
}