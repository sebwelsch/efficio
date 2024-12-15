package keac4.efficio.repository;

import keac4.efficio.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public Task findTaskById(int taskId) {
        String query = "SELECT * FROM tasks WHERE task_id = ?";
        List<Task> tasks = jdbcTemplate.query(query, new Object[]{taskId}, (rs, rowNum) ->
                new Task(
                        rs.getInt("task_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("expected_time"),
                        rs.getInt("subproject_id")
                ));
        return tasks.isEmpty() ? null : tasks.get(0);
    }

    public List<Task> getAllTasksBySubprojectId(int subprojectId) {
        String query = "SELECT * FROM tasks WHERE subproject_id = ?";
        return jdbcTemplate.query(query, new Object[]{subprojectId}, (rs, rowNum) ->
                new Task(
                        rs.getInt("task_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("expected_time"),
                        rs.getInt("subproject_id")
                ));
    }

    public int deleteTaskBySubprojectId(int taskId, int subprojectId) {
        String query = "DELETE FROM tasks WHERE task_id = ? AND subproject_id = ?";
        return jdbcTemplate.update(query, taskId, subprojectId);
    }

}
