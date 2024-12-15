package keac4.efficio.controller;

import keac4.efficio.model.User;
import keac4.efficio.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        User newUser = new User();
        newUser.setUsername("johnDoe");
        newUser.setPassword("123");
        userService.saveNewUser(newUser);
        User user = userService.findByUsername("johnDoe");
        session.setAttribute("userSession", user);
    }

    @AfterEach
    void tearDown() {
//        jdbcTemplate.update("DROP TABLE tasks");
//        jdbcTemplate.update("DROP TABLE subprojects");
//        jdbcTemplate.update("DROP TABLE project_users");
//        jdbcTemplate.update("DROP TABLE projects");
//        jdbcTemplate.update("DROP TABLE users");
    }

    @Test
    void showUserOverview() throws Exception {
        mockMvc.perform(get("/overview")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("userOverview"))
                .andExpect(model().attributeExists("projects"));
    }

    @Test
    void showCreateProjectPage() throws Exception {
        mockMvc.perform(get("/projects/create")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("createProject"))
                .andExpect(model().attributeExists("project"));
    }

    @Test
    void createProject() throws Exception {
        mockMvc.perform(post("/projects/create")
                        .session(session)
                        .param("name", "New Project")
                        .param("description", "Project description"))
                .andExpect(status().is3xxRedirection())  // Redirect after creation
                .andExpect(redirectedUrl("/overview"));
    }


    @Test
    void showProjectPage() throws Exception {
        mockMvc.perform(get("/projects/{projectId}", 1)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("projectOverview"))
                .andExpect(model().attributeExists("project"));
    }

    @Test
    void shareProject() {
    }

    @Test
    void showUpdateProjectPage() {
    }

    @Test
    void updateProject() {
    }

    @Test
    void deleteProject() {
    }

    @Test
    void showCreateSubprojectPage() {
    }

    @Test
    void createSubproject() {
    }

    @Test
    void showSubprojectPage() {
    }

    @Test
    void showUpdateSubprojectPage() {
    }

    @Test
    void updateSubproject() {
    }
}
