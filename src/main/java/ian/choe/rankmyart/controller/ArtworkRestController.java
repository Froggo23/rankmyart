package ian.choe.rankmyart.controller;

import ian.choe.rankmyart.model.Artwork;
import ian.choe.rankmyart.model.Comment;
import ian.choe.rankmyart.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import java.util.ArrayList;
import java.util.HashMap;
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
        // SQL to find user by username
        String sql = "SELECT * FROM users WHERE username = ?";

        try {

            Map<String, Object> row = jdbcTemplate.queryForMap(sql, username);

            User user = new User();

            user.setId((Integer) row.get("id"));
            user.setUsername((String) row.get("username"));
            user.setEmail((String) row.get("email"));
            user.setPassword((String) row.get("password"));

            return user;

        } catch (EmptyResultDataAccessException e) {
            // return null if user not found
            return null;
        }
    }

    @GetMapping("/api/artwork-details/{id}")
    public Map<String, Object> getArtworkAndComments(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();

        try {
            String artworkSql = "SELECT * FROM artworks WHERE id = ?";
            Artwork artwork = jdbcTemplate.queryForObject(artworkSql, new BeanPropertyRowMapper<>(Artwork.class), id);
            response.put("artwork", artwork);
        } catch (EmptyResultDataAccessException e) {
            response.put("error", "Artwork not found");
            return response;
        }

        String commentsSql = "SELECT * FROM comments WHERE artwork_id = ? ORDER BY created_at DESC";
        List<Comment> comments = jdbcTemplate.query(commentsSql, new BeanPropertyRowMapper<>(Comment.class), id);
        response.put("comments", comments);

        return response;
    }

    @PostMapping("/submitComment")
    public String createComment(@RequestBody Comment comment, HttpServletRequest request) {
        Cookie loginCookie = WebUtils.getCookie(request, "username");
        if (loginCookie == null) {
            return "needs login";
        }
        String author = loginCookie.getValue();

        Integer artworkId = comment.getArtworkId();

        String sql = "INSERT INTO comments (author, content, artwork_id, created_at) VALUES (?, ?, ?, NOW())";
        jdbcTemplate.update(sql, author, comment.getContent(), artworkId);
        return "success";
    }

    @PostMapping("/delete-comment")
    public String deleteComment(@RequestBody Comment comment, HttpServletRequest request) {
        Cookie loginCookie = WebUtils.getCookie(request, "username");
        if (loginCookie == null) {
            return "failed"; // Not logged in
        }
        String loggedInUser = loginCookie.getValue();

        String findSql = "SELECT author FROM comments WHERE id = ?";
        String actualAuthor;
        try {
            actualAuthor = jdbcTemplate.queryForObject(findSql, String.class, comment.getId());
        } catch (EmptyResultDataAccessException e) {
            return "failed"; // Comment doesn't exist
        }

        if (!loggedInUser.equals(actualAuthor)) {
            return "failed"; // Unauthorized
        }

        String deleteSql = "DELETE FROM comments WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(deleteSql, comment.getId());
        return rowsAffected > 0 ? "success" : "failed";
    }


    @GetMapping("/api/comments")
    public List<Comment> getCommentsForPost(@RequestParam("postId") int postId) {
        String sql = "SELECT * FROM comments WHERE artwork_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class), postId);
    }
}