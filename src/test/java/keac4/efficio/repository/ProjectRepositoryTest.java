package keac4.efficio.repository;

import keac4.efficio.model.Project;
import keac4.efficio.model.Subproject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active=test")
class ProjectRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProjectRepository projectRepository;

    private Project createdProject;

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
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createProject() {
        Project project = new Project();
        project.setName("Test project 2");
        project.setDescription("This is a project for test purposes that is different from the in in setUp()");
        project.setStartDate("2024-12-02");
        project.setDeadline("2024-12-25");

        int projectId = projectRepository.createProject(project);
        Project savedProject = projectRepository.getProjectById(projectId);

        assertAll("Test if all project variables are the same",
                () -> assertEquals(project.getName(), savedProject.getName()),
                () -> assertEquals(project.getDescription(), savedProject.getDescription()),
                () -> assertEquals(project.getStartDate(), savedProject.getStartDate()),
                () -> assertEquals(project.getDeadline(), savedProject.getDeadline()),
                () -> assertEquals(project.getExpectedTime(), savedProject.getExpectedTime())
        );
    }


    @Test
    void createSubproject() {
        Subproject subproject = new Subproject();
        subproject.setName("Test subproject 2");
        subproject.setDescription("This is a subproject for test purposes and is different from the one in setUp()");
        subproject.setStartDate("2024-12-12");
        subproject.setDeadline("2024-12-20");

        projectRepository.createSubproject(subproject, createdProject.getProjectId());
        List<Subproject> savedSubprojects = projectRepository.getAllSubprojectsByProjectId(createdProject.getProjectId());

        assertAll("Test if all subproject variables are the same",
                () -> assertEquals(subproject.getName(), savedSubprojects.get(1).getName()),
                () -> assertEquals(subproject.getDescription(), savedSubprojects.get(1).getDescription()),
                () -> assertEquals(subproject.getStartDate(), savedSubprojects.get(1).getStartDate()),
                () -> assertEquals(subproject.getDeadline(), savedSubprojects.get(1).getDeadline()),
                () -> assertEquals(subproject.getExpectedTime(), savedSubprojects.get(1).getExpectedTime())
        );
    }

    @Test
    void updateProject() {
        Project updatedProject = new Project();
        updatedProject.setName("New title for test project");
        updatedProject.setDescription("I have updated the description of this project");
        updatedProject.setStartDate("2025-05-12");
        updatedProject.setDeadline("2025-11-25");
        // Update the project that was made in setUp()
        updatedProject.setProjectId(createdProject.getProjectId());

        projectRepository.updateProject(updatedProject);
        Project savedProject = projectRepository.getProjectById(createdProject.getProjectId());
        assertAll("Test if all updated project variables are the same",
                () -> assertEquals(updatedProject.getName(), savedProject.getName()),
                () -> assertEquals(updatedProject.getDescription(), savedProject.getDescription()),
                () -> assertEquals(updatedProject.getStartDate(), savedProject.getStartDate()),
                () -> assertEquals(updatedProject.getDeadline(), savedProject.getDeadline()),
                () -> assertEquals(updatedProject.getExpectedTime(), savedProject.getExpectedTime())
        );
    }

    @Test
    void deleteProjectById() {
        projectRepository.deleteProjectById(createdProject.getProjectId());

        Exception exception = assertThrows(Exception.class, () -> projectRepository.getProjectById(createdProject.getProjectId()));

        assertNotNull(exception);
    }
}