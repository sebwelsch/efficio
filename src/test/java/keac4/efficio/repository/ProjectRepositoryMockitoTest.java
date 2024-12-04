package keac4.efficio.repository;

import keac4.efficio.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("dev")
public class ProjectRepositoryMockitoTest {

    //Three methods when testing a class
    //Arrange: Inputs and targets. Setting up the test
    //Act: Acting on the behavior.
    //Assert: Expected outcomes. Making sure that it does what it is supposed to do.

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ProjectRepository projectRepository;  // The class we are testing

    @BeforeEach
    void setup() { // Initializes mocks before each test making sure it has data.
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testCreateProject() {
//        // Arrange: Create a mock project (fake project) and giving it values
//        Project newProject = new Project();  // Make an instance of the class
//        newProject.setName("Test Project");
//        newProject.setDescription("Test description");
//        newProject.setStartDate("2024-01-01");
//        newProject.setDeadline("2024-12-31");
//        newProject.setExpectedTime(100);
//
//        // Mocking GeneratedKeyHolder and JdbcTemplate update method
//        KeyHolder mockKeyHolder = mock(GeneratedKeyHolder.class);
//        when(mockKeyHolder.getKey()).thenReturn(1); // Simulating generated key (ID = 1)
//
//        // Mock the behavior of jdbcTemplate.update method (simulate inserting the project)
//        when(jdbcTemplate.update(any(), eq(mockKeyHolder))).thenReturn(1); //eq = exactly so it makes sure that only the exact keyholder gets the value
//
//        // Act: Call the createProject method of the ProjectRepository
//        int generatedId = projectRepository.createProject(newProject);
//
//        // Assert: Verify that the returned ID is correct
//        assertEquals(1, generatedId);  // We expect the ID to be 1
//
//        // Verify the query passed to jdbcTemplate. Aka making sure it does what it is told.
//        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class); //Mockito utility that is used to capture arguments. forClass string captures only an SQL string. queryCaptor arguments that holds the answer.
//        verify(jdbcTemplate).update(queryCaptor.capture(), eq(mockKeyHolder)); //Simply tests if the method was called. queryCaptor.capture() captures the first passed update (the sql string)
//
//        // Assert that the correct SQL query is used
//        String capturedQuery = queryCaptor.getValue(); //once again making sure the correct one is used.
//        assertEquals("INSERT INTO projects (name, description, start_date, deadline, expected_time) VALUES (?, ?, ?, ?, ?)", capturedQuery);
//
//        // Verify that the generated key was retrieved from the KeyHolder
//        verify(mockKeyHolder).getKey();
//    }
}
