package keac4.efficio.repository;

import keac4.efficio.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;


    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void save(User newUser) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        jdbcTemplate.update(query, newUser.getUsername(), newUser.getPassword());
    }


    public User findByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{username}, (rs, rowNum) ->
                new User(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getString("password")
                )
        );
    }
}