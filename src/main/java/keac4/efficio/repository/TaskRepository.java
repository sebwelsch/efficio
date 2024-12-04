package keac4.efficio.repository;

import keac4.efficio.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveNewTask(Task newTask) {
        String query = "INSERT INTO tasks (subproject_id, name, description, expected_time) VALUES (?, ?, ?, ?)";
        System.out.println("Executing query: " + query);
        jdbcTemplate.update(query, newTask.getSubprojectId(), newTask.getName(), newTask.getDescription(), newTask.getExpectedTime());
    }

}
