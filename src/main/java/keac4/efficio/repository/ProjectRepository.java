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

    // Link the project to a user by adding an entry in the project_users table
    public boolean linkProjectToUser(int projectId, int userId) {
        String checkQuery = "SELECT COUNT(*) FROM project_users WHERE project_id = ? AND user_id = ?";
        int count = jdbcTemplate.queryForObject(checkQuery, Integer.class, projectId, userId);

        if (count == 0) {
            String query = "INSERT INTO project_users (project_id, user_id) VALUES (?, ?)";
            jdbcTemplate.update(query, projectId, userId);
            return true;
        } else {
            return false;
        }
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

    public void updateProject(Project project) {
        String query = "UPDATE projects SET name = ?, description = ?, start_date = ?, deadline = ? WHERE project_id = ?";
        jdbcTemplate.update(query, project.getName(), project.getDescription(), project.getStartDate(), project.getDeadline(), project.getProjectId());
    }

    public void updateSubproject(Subproject subproject) {
        String query = "UPDATE subprojects SET name = ?, description = ?, start_date = ?, deadline = ? WHERE subproject_id = ?";
        jdbcTemplate.update(query, subproject.getName(), subproject.getDescription(), subproject.getStartDate(), subproject.getDeadline(), subproject.getSubprojectId());
    }

    //Find by userId for the overview-html.
    //p* select all from projects. p stands for projects
    //Inner join combines the two tables p and pu (project_users)
    //The join has the condition of p.project_id = pu.project_id. It needs to match
    //WHERE ensures that only the actual user can do it.
    public List<Project> findByUserID(int userId) {
        String query = "SELECT p.* FROM projects p "
                + "INNER JOIN project_users pu ON p.project_id = pu.project_id "
                + "WHERE pu.user_id = ?";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Project.class), userId);
    }

    public void deleteProjectById(int projectId) {
        String query = "DELETE FROM projects WHERE project_id = ?";
        jdbcTemplate.update(query, projectId);
    }

    public void deleteSubprojectById(int subprojectId) {
        String query = "DELETE FROM subprojects WHERE subproject_id = ?";
        jdbcTemplate.update(query, subprojectId);
    }

    public void updateExpectedTimeSubproject(int subprojectId, int newExpectedTime) {
        String query = "UPDATE subprojects SET expected_time = ? WHERE subproject_id = ?";
        jdbcTemplate.update(query, newExpectedTime, subprojectId);
    }

    public void updateExpectedTimeProject(int projectId, int newExpectedTime) {
        String query = "UPDATE projects SET expected_time = ? WHERE project_id = ?";
        jdbcTemplate.update(query, newExpectedTime, projectId);
    }
}
