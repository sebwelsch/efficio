package keac4.efficio.repository;

import keac4.efficio.model.Project;
import keac4.efficio.model.Subproject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active=test")
class ProjectRepositoryTest {

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
        return projectRepository.getProjectById(projectId);
    }

    private Subproject createTestSubproject() {
        Subproject subproject = new Subproject();
        subproject.setName("Test subproject 2");
        subproject.setDescription("This is a subproject for test purposes and is different from the one in setUp()");
        subproject.setStartDate("2024-12-12");
        subproject.setDeadline("2024-12-20");

        projectRepository.createSubproject(subproject, createdProject.getProjectId());
        return projectRepository.getAllSubprojectsByProjectId(createdProject.getProjectId()).getFirst();
    }

    @Test
    void createProject() {
        createdProject.setName("Test project 2");
        createdProject.setDescription("NEw description");
        createdProject.setStartDate("2024-12-25");
        createdProject.setDeadline("2024-12-31");

        int projectId = projectRepository.createProject(createdProject);
        Project savedProject = projectRepository.getProjectById(projectId);

        assertAll("Test if all project variables are the same",
                () -> assertEquals(createdProject.getName(), savedProject.getName()),
                () -> assertEquals(createdProject.getDescription(), savedProject.getDescription()),
                () -> assertEquals(createdProject.getStartDate(), savedProject.getStartDate()),
                () -> assertEquals(createdProject.getDeadline(), savedProject.getDeadline())
        );
    }

    @Test
    void createSubproject() {
        createdSubproject.setName("Updated Test subproject");
        createdSubproject.setDescription("NEw description");
        createdSubproject.setStartDate("2024-12-14");
        createdSubproject.setDeadline("2024-12-22");

        projectRepository.createSubproject(createdSubproject, createdProject.getProjectId());
        Subproject savedSubproject = projectRepository.getAllSubprojectsByProjectId(createdProject.getProjectId()).get(1);

        assertAll("Test if all subproject variables are the same",
                () -> assertEquals(createdSubproject.getName(), savedSubproject.getName()),
                () -> assertEquals(createdSubproject.getDescription(), savedSubproject.getDescription()),
                () -> assertEquals(createdSubproject.getStartDate(), savedSubproject.getStartDate()),
                () -> assertEquals(createdSubproject.getDeadline(), savedSubproject.getDeadline())
        );
    }

    @Test
    void updateProject() {
        createdProject.setName("new name for Test project");
        createdProject.setDescription("another new description");
        createdProject.setStartDate("2025-01-01");
        createdProject.setDeadline("2025-01-09");

        projectRepository.updateProject(createdProject);
        Project savedProject = projectRepository.getProjectById(createdProject.getProjectId());

        assertAll("Test if all project variables are the same",
                () -> assertEquals(createdProject.getName(), savedProject.getName()),
                () -> assertEquals(createdProject.getDescription(), savedProject.getDescription()),
                () -> assertEquals(createdProject.getStartDate(), savedProject.getStartDate()),
                () -> assertEquals(createdProject.getDeadline(), savedProject.getDeadline())
        );
    }

    @Test
    void deleteProjectById() {
        projectRepository.deleteProjectById(createdProject.getProjectId());

        assertThrows(Exception.class, () -> projectRepository.getProjectById(createdProject.getProjectId()));
    }
}
