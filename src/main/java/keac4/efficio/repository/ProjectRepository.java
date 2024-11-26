package keac4.efficio.repository;

import keac4.efficio.model.Project;
import keac4.efficio.model.Subproject;
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

    public List<Subproject> getSubProjectsByProjectId(int projectId) {
        String query = "SELECT * FROM subprojects WHERE project_id = ?";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Subproject.class));
    }

    public boolean doesUserHaveAccess(int projectId, int userId) {
        String query = "SELECT COUNT(*) FROM project_users WHERE project_id = ? AND user_id = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, projectId, userId);
        return count != null && count > 0;
    }

    public Project getProjectById(int projectId) {
        String query = "SELECT * FROM projects WHERE project_id = ?";
        return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Project.class), projectId);
    }
    

}
