package ian.choe.rankmyart.domain.artwork.controller;

import ian.choe.rankmyart.domain.artwork.service.ArtworkService;
import ian.choe.rankmyart.model.ArtworkDetailsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArtworkDetailsController {

    private final ArtworkService artworkService;

    @Autowired
    public ArtworkDetailsController(ArtworkService artworkService) {
        this.artworkService = artworkService;
    }

    @GetMapping("/api/artwork-details/{id}")
    public ResponseEntity<ArtworkDetailsDto> getArtworkDetails(@PathVariable int id) {
        ArtworkDetailsDto artworkDetails = artworkService.getArtworkDetails(id);
        if (artworkDetails == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(artworkDetails);
    }
}