package ian.choe.rankmyart.domain.artwork.service;

import ian.choe.rankmyart.domain.artwork.repository.ArtworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    private final ArtworkRepository artworkRepository;

    @Autowired
    public VoteService(ArtworkRepository artworkRepository) {
        this.artworkRepository = artworkRepository;
    }

    public void incrementVote(int id) {
        int prevVote = artworkRepository.getUpVotes(id);
        int newVote = prevVote + 1;
        artworkRepository.setUpVotes(id, newVote);
    }

    public void decrementVote(int id) {
        int prevVote = artworkRepository.getUpVotes(id);
        int newVote = prevVote - 1;
        artworkRepository.setUpVotes(id, newVote);
    }
}
