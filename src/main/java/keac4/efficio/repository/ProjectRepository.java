package keac4.efficio.repository;

import keac4.efficio.model.Project;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addProject(Project newProject) {
        String query = "INSERT INTO projects (name, description, start_date, deadline, expected_time) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query,
                newProject.getName(),
                newProject.getDescription(),
                newProject.getStartDate(),
                newProject.getDeadline(),
                newProject.getExpectedTime());
    }

    public List<Project> findAll() {
        String query = "SELECT * FROM projects";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Project.class));
    }

}
