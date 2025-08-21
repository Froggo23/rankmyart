package ian.choe.rankmyart.domain.artwork.repository;

import ian.choe.rankmyart.model.Artwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArtworkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ArtworkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Artwork> findArtworksByQuery(String query, int count, int start) {
        String sql = "SELECT * FROM public.artworks WHERE title ILIKE ? OR description ILIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Artwork.class), "%" + query + "%", "%" + query + "%", count, start);
    }

    public List<Artwork> findAllArtworks(int count, int start) {
        String sql = "SELECT * FROM artworks ORDER BY id DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Artwork.class), count, start);
    }

    public long countArtworksByQuery(String query) {
        String sql = "SELECT COUNT(*) FROM artworks WHERE title ILIKE ? OR description ILIKE ?";
        return jdbcTemplate.queryForObject(sql, Long.class, "%" + query + "%", "%" + query + "%");
    }

    public long countAllArtworks() {
        String sql = "SELECT COUNT(*) FROM artworks";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public Artwork findArtworkById(int artworkId) {
        String sql = "SELECT a.*, u.username AS uploaderUsername, u.profile_img AS uploaderProfileImg " +
                "FROM artworks a " +
                "LEFT JOIN users u ON a.uploader_username = u.username " +
                "WHERE a.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Artwork.class), artworkId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int getUpVotes(int id) {
        String sql = "SELECT upvotes FROM artworks WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id);
    }

    public void setUpVotes(int id, int upvotes) {
        String sql = "UPDATE artworks SET upvotes = ? WHERE id = ?";
        jdbcTemplate.update(sql, upvotes, id);
    }

    public void save(String title, String tags, String description, String imageUrl, String username) {
        String sql = "INSERT INTO artworks (title, category, description, image_url, uploader_username) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, title, tags, description, imageUrl, username);
    }

    public List<Artwork> findArtworksByUploaderUsername(String username) {
        String sql = "SELECT * FROM artworks WHERE uploader_username = ? ORDER BY id DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Artwork.class), username);
    }
}
