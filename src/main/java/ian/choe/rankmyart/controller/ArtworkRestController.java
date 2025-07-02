package ian.choe.rankmyart.controller;

import ian.choe.rankmyart.model.Artwork;
import ian.choe.rankmyart.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ArtworkRestController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/gallery/infinite")
    public List<Artwork> getArtworks(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "query", required = false) String query) {

        int count = 5;
        int start = (page - 1) * count;
        List<Artwork> artworkList = new ArrayList<>();
        String sql;
        List<Map<String, Object>> rows;

        if (query != null && !query.isEmpty()) {
            sql = "SELECT * FROM public.artworks WHERE title ILIKE ? OR description ILIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
            rows = jdbcTemplate.queryForList(sql, "%" + query + "%", "%" + query + "%", count, start);
        } else {
            sql = "SELECT * FROM artworks ORDER BY id DESC LIMIT ? OFFSET ?";
            rows = jdbcTemplate.queryForList(sql, count, start);
        }

        for (Map<String, Object> row : rows) {
            Artwork artwork = new Artwork();
            artwork.setId((Integer) row.get("id"));
            artwork.setTitle((String) row.get("title"));
            artwork.setCategory((String) row.get("category"));
            artwork.setDescription((String) row.get("description"));
            artwork.setImageUrl((String) row.get("image_url"));
            artwork.setUpvotes((Integer) row.get("upvotes"));
            artwork.setViews((Integer) row.get("views"));
            artworkList.add(artwork);
        }

        return artworkList;
    }

    @GetMapping("/user")
    public User getUser(@RequestParam(name = "username") String username) {
        // SQL to find a user by their username in the 'users' table
        String sql = "SELECT * FROM users WHERE username = ?";

        try {
            // queryForMap expects a single result. It's perfect for finding a unique user.
            Map<String, Object> row = jdbcTemplate.queryForMap(sql, username);

            User user = new User();

            // Map the database columns to your User object fields.
            // Note: These field names ('id', 'email', 'bio', etc.) are assumed.
            // Make sure they match the actual columns in your 'users' table and fields in your User model.
            user.setId((Integer) row.get("id"));
            user.setUsername((String) row.get("username"));
            user.setEmail((String) row.get("email"));
            user.setPassword((String) row.get("password"));

            return user;

        } catch (EmptyResultDataAccessException e) {
            // This block runs if no user with that username is found.
            // Returning null will result in an empty or 404 response, which is appropriate.
            return null;
        }
    }

    @GetMapping("/artworks/{id}")
    public Artwork getArtworkById(@PathVariable int id) {
        String sql = "SELECT * FROM artworks WHERE id = ?";
        try {
            // Use queryForMap as we expect exactly one result for a given ID.
            Map<String, Object> row = jdbcTemplate.queryForMap(sql, id);

            Artwork artwork = new Artwork();
            artwork.setId((Integer) row.get("id"));
            artwork.setTitle((String) row.get("title"));
            artwork.setCategory((String) row.get("category"));
            artwork.setDescription((String) row.get("description"));
            artwork.setImageUrl((String) row.get("image_url"));
            artwork.setUpvotes((Integer) row.get("upvotes"));
            artwork.setViews((Integer) row.get("views"));

            return artwork;

        } catch (EmptyResultDataAccessException e) {
            // If queryForMap finds no rows, it throws this exception.
            // We return null to indicate the artwork was not found.
            return null;
        }
    }
}