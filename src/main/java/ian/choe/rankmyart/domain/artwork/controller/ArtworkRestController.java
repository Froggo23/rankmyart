package ian.choe.rankmyart.domain.artwork.controller;

import ian.choe.rankmyart.domain.artwork.service.ArtworkService;
import ian.choe.rankmyart.model.Artwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArtworkRestController {

    private final ArtworkService artworkService;

    @Autowired
    public ArtworkRestController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    @GetMapping("/gallery/infinite")
    public List<Artwork> getArtworks(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "query", required = false) String query) {
        return artworkService.getArtworks(page, query);
    }

    @GetMapping("/artworks/{id}")
    public Artwork getArtworkById(@PathVariable int id) {
        return artworkService.getArtworkById(id);
    }
}