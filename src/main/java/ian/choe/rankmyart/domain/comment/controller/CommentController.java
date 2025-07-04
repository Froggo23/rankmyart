package ian.choe.rankmyart.domain.comment.controller;

import ian.choe.rankmyart.domain.comment.service.CommentService;
import ian.choe.rankmyart.model.Comment;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

// Import the Map class
import java.util.Map;

@RestController
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/submitComment")
    public String createComment(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        Cookie loginCookie = WebUtils.getCookie(request, "username"); // Using "username" cookie for consistency
        if (loginCookie == null) {
            return "needs login";
        }

        // Manually extract data from the payload Map
        String content = payload.get("content");
        int artworkId = Integer.parseInt(payload.get("artworkId"));

        Comment newComment = new Comment();
        newComment.setArtworkId(artworkId);
        newComment.setContent(content);
        newComment.setAuthor(loginCookie.getValue());

        boolean success = commentService.createComment(newComment);
        return success ? "success" : "failed";
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        Cookie loginCookie = WebUtils.getCookie(request, "username");
        if (loginCookie == null) {
            return "failed";
        }

        int commentId = Integer.parseInt(payload.get("id"));
        String commentAuthor = payload.get("author");
        String loggedInUser = loginCookie.getValue();

        if (!loggedInUser.equals(commentAuthor)) {
            return "failed";
        }

        boolean deleted = commentService.deleteComment(commentId);
        return deleted ? "success" : "failed";
    }
}