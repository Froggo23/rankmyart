package ian.choe.rankmyart.service;

import ian.choe.rankmyart.repository.ArtworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {


    private ArtworkRepository artworkRepository;

    public VoteService(ArtworkRepository artworkRepository) {
        this.artworkRepository = artworkRepository;
    }

    public void incrementVote(int id){
        int prevVote = artworkRepository.getUpVotes(id);
        int newVote = prevVote + 1;
        artworkRepository.setUpVotes(id, newVote);
    }

    public void decrementVote(int id){
        int prevVote = artworkRepository.getUpVotes(id);
        int newVote = prevVote - 1;
        artworkRepository.setUpVotes(id, newVote);
    }

}
