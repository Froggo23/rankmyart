package ian.choe.rankmyart.domain.user.repository;

import ian.choe.rankmyart.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Saves a new user to the database, including their profile image URL and bio.
     * This method is used during user registration.
     * @param user The User object to save.
     */
    public void save(User user) {
        String sql = "INSERT INTO users (username, email, password, profile_img, bio) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPassword(), user.getProfileImg(), user.getBio());
    }

    /**
     * Finds a user by their username.
     * Retrieves all user fields, including profile_img and bio.
     * @param username The username to search for.
     * @return The User object if found, otherwise null.
     */
    public User findByUsername(String username) {
        String sql = "SELECT id, username, email, password, profile_img, bio FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Finds a user by their email.
     * Retrieves all user fields, including profile_img and bio.
     * @param email The email to search for.
     * @return The User object if found, otherwise null.
     */
    public User findByEmail(String email) {
        String sql = "SELECT id, username, email, password, profile_img, bio FROM users WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Updates the profile image URL and bio for a given user.
     * @param username The username of the user to update.
     * @param profileImg The new profile image URL.
     * @param bio The new bio text.
     */
    public void updateUserProfile(String username, String profileImg, String bio) {
        String sql = "UPDATE users SET profile_img = ?, bio = ? WHERE username = ?";
        jdbcTemplate.update(sql, profileImg, bio, username);
    }
}
