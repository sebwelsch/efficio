package keac4.efficio.repository;

import keac4.efficio.model.Project;
import keac4.efficio.model.Subproject;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ProjectRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createProject(Project newProject) {
        String query = "INSERT INTO projects (name, description, start_date, deadline, expected_time) VALUES (?, ?, ?, ?, ?)";

        // Insert the project and retrieve the generated ID. It needs to be written like this as it needs the generated keys so that it can pass those values onto the next holder.
        // Thereby correctly associating the created project with the correct user.
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newProject.getName());
            ps.setString(2, newProject.getDescription());
            ps.setString(3, newProject.getStartDate());
            ps.setString(4, newProject.getDeadline());
            ps.setInt(5, newProject.getExpectedTime());
            return ps;
        }, keyHolder);

        // Return the generated project ID
        return keyHolder.getKey().intValue();
    }

    public void createSubproject(Subproject subproject, int projectId) {
        String query = "INSERT INTO subprojects (project_id, name, description, start_date, deadline, expected_time) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(query,
                projectId,
                subproject.getName(),
                subproject.getDescription(),
                subproject.getStartDate(),
                subproject.getDeadline(),
                subproject.getExpectedTime()
                );
    }

    // Link the project to the user by adding an entry in the project_users table
    public void linkProjectToUser(int projectId, int userId) {
        String query = "INSERT INTO project_users (project_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(query, projectId, userId);
    }

    public List<Project> findAll() {
        String query = "SELECT * FROM projects";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Project.class));
    }

    public List<Subproject> getAllSubprojectsByProjectId(int projectId) {
        String query = "SELECT * FROM subprojects WHERE project_id = ?";
        return jdbcTemplate.query(query, new Object[]{projectId}, (rs, rowNum) ->
                new Subproject(
                        rs.getInt("subproject_id"),
                        rs.getInt("project_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("start_date"),
                        rs.getString("deadline"),
                        rs.getInt("expected_time")
                ));
    }

    public Project getProjectById(int projectId) {
        String query = "SELECT * FROM projects WHERE project_id = ?";
        return jdbcTemplate.queryForObject(query, new Integer[]{projectId}, (rs, rowNum) ->
                new Project(
                        rs.getInt("project_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("start_date"),
                        rs.getString("deadline"),
                        rs.getInt("expected_time")
                ));
    }

    public Subproject getSubprojectById(int subprojectId) {
        String query = "SELECT * FROM subprojects WHERE subproject_id = ?";
        return jdbcTemplate.queryForObject(query, new Integer[]{subprojectId}, (rs, rowNum) ->
                new Subproject(
                        rs.getInt("subproject_id"),
                        rs.getInt("project_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("start_date"),
                        rs.getString("deadline"),
                        rs.getInt("expected_time")
                ));
    }

    public List<Project> findByUserID(int userId) {
        String query = "SELECT * FROM project_users WHERE user_id = ?";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Project.class), userId);
    }

}
