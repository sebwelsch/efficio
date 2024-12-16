package keac4.efficio.repository;

import keac4.efficio.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;


    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveNewUser(User newUser) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        jdbcTemplate.update(query, newUser.getUsername(), newUser.getPassword());
    }

    public User findByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(query, new Object[]{username}, (rs, rowNum) ->
                    new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password")
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean doesUserHaveAccess(int projectId, int userId) {
        String query = "SELECT COUNT(*) FROM project_users WHERE project_id = ? AND user_id = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, projectId, userId);
        return count != null && count > 0;
    }
}