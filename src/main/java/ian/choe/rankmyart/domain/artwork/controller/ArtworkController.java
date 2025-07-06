package ian.choe.rankmyart.domain.artwork.controller;

import ian.choe.rankmyart.domain.artwork.service.ArtworkService;
import ian.choe.rankmyart.domain.comment.service.CommentService;
import ian.choe.rankmyart.model.Artwork;
import ian.choe.rankmyart.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ArtworkController {

    private final ArtworkService artworkService;
    private final CommentService commentService;

    @Autowired
    public ArtworkController(ArtworkService artworkService, CommentService commentService) {
        this.artworkService = artworkService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String redirectToGallery() {
        return "redirect:/gallery";
    }

    @GetMapping("/gallery")
    public String board(Model model,
                        @RequestParam(name = "page", defaultValue = "1") int page,
                        @RequestParam(name = "query", required = false) String query) {

        List<Artwork> artworkList = artworkService.getArtworks(page, query);
        int totalPages = artworkService.getTotalPages(query);

        model.addAttribute("artworkList", artworkList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("query", query);

        return "index";
    }

    @GetMapping("/artinfo")
    public String showArtInfoPage(@RequestParam("id") int artworkId, Model model) {
        return "artinfo";
    }
}

