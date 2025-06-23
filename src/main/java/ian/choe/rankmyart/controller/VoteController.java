package ian.choe.rankmyart.controller;

import ian.choe.rankmyart.service.VoteService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteController {

    private VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/upvote")
    public void upvote(@RequestParam int id) {
        voteService.incrementVote(id);
    }

    @PostMapping("/downvote" )
    public void downvote(@RequestParam int id) {
        voteService.decrementVote(id);
    }

}
