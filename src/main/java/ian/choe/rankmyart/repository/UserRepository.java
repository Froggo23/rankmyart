package ian.choe.rankmyart.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ian.choe.rankmyart.model.User;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(User user) {
        // WARNING: Storing plain text password. HIGH SECURITY RISK.
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPassword());
    }

    public User findByUsername(String username) {
        String sql = "SELECT id, username, email, password FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password")); // Fetching plain text password
                return user;
            }, username);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }

    public User findByEmail(String email) {
        String sql = "SELECT id, username, email, password FROM users WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password")); // Fetching plain text password
                return user;
            }, email);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        }
    }
}