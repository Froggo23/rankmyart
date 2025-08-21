package ian.choe.rankmyart.domain.artwork.service;

import ian.choe.rankmyart.domain.artwork.repository.ArtworkRepository;
import ian.choe.rankmyart.domain.comment.service.CommentService;
import ian.choe.rankmyart.model.Artwork;
import ian.choe.rankmyart.model.ArtworkDetailsDto;
import ian.choe.rankmyart.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final CommentService commentService;

    @Autowired
    public ArtworkService(ArtworkRepository artworkRepository, CommentService commentService) {
        this.artworkRepository = artworkRepository;
        this.commentService = commentService;
    }

    public List<Artwork> getArtworks(int page, String query) {
        int count = 5;
        int start = (page - 1) * count;
        if (query != null && !query.isEmpty()) {
            return artworkRepository.findArtworksByQuery(query, count, start);
        } else {
            return artworkRepository.findAllArtworks(count, start);
        }
    }

    public int getTotalPages(String query) {
        int count = 5;
        long totalArtworks;
        if (query != null && !query.isEmpty()) {
            totalArtworks = artworkRepository.countArtworksByQuery(query);
        } else {
            totalArtworks = artworkRepository.countAllArtworks();
        }
        return (int) Math.ceil((double) totalArtworks / count);
    }

    public Artwork getArtworkById(int artworkId) {
        return artworkRepository.findArtworkById(artworkId);
    }

    public void saveArtwork(String title, String tags, String description, String imageUrl, String username) {
        artworkRepository.save(title, tags, description, imageUrl, username);
    }

    public ArtworkDetailsDto getArtworkDetails(int artworkId) {
        Artwork artwork = artworkRepository.findArtworkById(artworkId);
        if (artwork == null) {
            return null;
        }
        List<Comment> comments = commentService.getCommentsByArtworkId(artworkId);
        return new ArtworkDetailsDto(artwork, comments);
    }

    public List<Artwork> getArtworksByUploader(String username) {
        return artworkRepository.findArtworksByUploaderUsername(username);
    }
}