package ian.choe.rankmyart.domain.artwork.controller;

import ian.choe.rankmyart.domain.artwork.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteController {

    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/upvote")
    public void upvote(@RequestParam int id) {
        voteService.incrementVote(id);
    }

    @PostMapping("/downvote")
    public void downvote(@RequestParam int id) {
        voteService.decrementVote(id);
    }
}
