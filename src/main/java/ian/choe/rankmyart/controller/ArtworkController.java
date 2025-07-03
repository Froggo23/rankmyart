package ian.choe.rankmyart.controller;

import ian.choe.rankmyart.model.Artwork;
import ian.choe.rankmyart.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class ArtworkController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String redirectToGallery() {
        return "redirect:/gallery";
    }

    @GetMapping("/gallery")
    public String board(Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page,
                        @RequestParam(name = "query", required = false) String query) {


        // Pagination
        int count = 5;
        int start = (page - 1) * count;
        List<Artwork> artworkList = new ArrayList<>();
        String sql;
        int totalArtworks;

        // Search function
        if (query != null && !query.isEmpty()) {
            sql = "SELECT * FROM public.artworks WHERE title ILIKE ? OR description ILIKE ? ORDER BY id DESC LIMIT ? OFFSET ?";
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, "%" + query + "%", "%" + query + "%", count, start);

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

            totalArtworks = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM artworks WHERE title ILIKE ? OR description ILIKE ?",
                    Integer.class,
                    "%" + query + "%", "%" + query + "%"
            );
        } else {
            sql = "SELECT * FROM artworks ORDER BY id DESC LIMIT ? OFFSET ?";
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, count, start);

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

            totalArtworks = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM artworks", Integer.class);
        }

        int totalPages = (int) Math.ceil((double) totalArtworks / count);

        // Add data
        model.addAttribute("artworkList", artworkList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("query", query);

        return "index";
    }

    @GetMapping("/artinfo")
    public String showArtInfoPage(@RequestParam("id") int artworkId, Model model) {
        // Fetch the artwork details
        try {
            String artworkSql = "SELECT * FROM artworks WHERE id = ?";
            Artwork artwork = jdbcTemplate.queryForObject(artworkSql, new BeanPropertyRowMapper<>(Artwork.class), artworkId);
            model.addAttribute("artwork", artwork);
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/gallery"; // artwork not found
        }

        // fetch comment
        String commentsSql = "SELECT * FROM comments WHERE artwork_id = ? ORDER BY created_at DESC";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(commentsSql, artworkId);
        List<Comment> comments = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            Comment comment = new Comment();
            comment.setId((Integer) row.get("id"));
            comment.setArtworkId((Integer) row.get("artwork_id"));
            comment.setContent((String) row.get("content"));
            comment.setAuthor((String) row.get("author"));
            comment.setCreatedAt((Date) row.get("created_at"));

            Object isEdited = row.get("is_edited");
            comment.setEdited(isEdited != null && (boolean) isEdited);

            comments.add(comment);
        }

        model.addAttribute("comments", comments);

        return "artinfo";
    }
}

