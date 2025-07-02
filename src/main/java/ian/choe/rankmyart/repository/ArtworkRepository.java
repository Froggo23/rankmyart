package ian.choe.rankmyart.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ArtworkRepository {

    private final JdbcTemplate jdbcTemplate;

    public ArtworkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getUpVotes(int id) {
        String sql = "SELECT upvotes FROM artworks WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id);
    }

    public void setUpVotes(int id, int upvotes) {
        String sql = "UPDATE artworks SET upvotes = ? WHERE id = ?";
        jdbcTemplate.update(sql, upvotes, id);
    }



}
