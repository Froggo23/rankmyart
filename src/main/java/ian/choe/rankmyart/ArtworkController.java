package ian.choe.rankmyart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ArtworkController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/gallery")
    public String board(Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page,
                        @RequestParam(name = "query", required = false) String query,
                        HttpServletRequest request) {
        // 아직 로그인은 안만듬
        // cookies
        // Cookie loginCookie = WebUtils.getCookie(request, "login_id");
        // if (loginCookie == null) {
        //     return "redirect:/login";
        // }

        // Pagination
        int count = 10;
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

        return "gallery";
    }
}