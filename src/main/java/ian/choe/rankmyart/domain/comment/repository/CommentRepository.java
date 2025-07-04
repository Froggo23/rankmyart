package ian.choe.rankmyart.domain.comment.repository;

import ian.choe.rankmyart.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Comment> findCommentsByArtworkId(int artworkId) {
        String sql = "SELECT * FROM comments WHERE artwork_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class), artworkId);
    }

    public Comment findById(int commentId) {
        String sql = "SELECT * FROM comments WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Comment.class), commentId);
        } catch (Exception e) {
            return null; // Return null if not found
        }
    }

    public boolean save(Comment comment) {
        String sql = "INSERT INTO comments (author, content, artwork_id, created_at) VALUES (?, ?, ?, NOW())";
        int rowsAffected = jdbcTemplate.update(sql, comment.getAuthor(), comment.getContent(), comment.getArtworkId());
        return rowsAffected > 0;
    }

    public boolean deleteById(int commentId) {
        String sql = "DELETE FROM comments WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, commentId);
        return rowsAffected > 0;
    }
}
