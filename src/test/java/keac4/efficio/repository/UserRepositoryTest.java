package keac4.efficio.repository;

import keac4.efficio.model.Project;
import keac4.efficio.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active=test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProjectRepository projectRepository;

    private Project testProject;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        testProject = createTestProject();
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

    @Test
    void saveNewUser() {
        User user = new User();
        user.setUsername("testUser2");
        user.setPassword(passwordEncoder.encode("abc123"));
        userRepository.saveNewUser(user);

        User savedUser = userRepository.findByUsername("testUser2");
        assertNotNull(savedUser);
        // Check that password is hashed
        assertNotEquals("abc123", savedUser.getPassword());
        assertTrue(passwordEncoder.matches("abc123", savedUser.getPassword()));
    }

    @Test
    void linkProjectToUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword(passwordEncoder.encode("123"));
        userRepository.saveNewUser(user);
        User testUser1 = userRepository.findByUsername(user.getUsername());

        projectRepository.linkProjectToUser(testProject.getProjectId(), testUser1.getUserId());

        boolean userHasAccess = userRepository.doesUserHaveAccess(testProject.getProjectId(), testUser1.getUserId());

        assertTrue(userHasAccess);
    }
}