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

@RestController
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/submitComment")
    public String createComment(@RequestBody Comment comment, HttpServletRequest request) {
        Cookie loginCookie = WebUtils.getCookie(request, "login_id");
        if (loginCookie == null) {
            return "needs login";
        }

        String author = loginCookie.getValue();
        comment.setAuthor(author);

        commentService.createComment(comment);

        return "success";
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestBody Comment comment, HttpServletRequest request) {
        Cookie loginCookie = WebUtils.getCookie(request, "username");
        if (loginCookie == null) {
            return "failed"; // Not logged in
        }

        String loggedInUser = loginCookie.getValue();
        if (!loggedInUser.equals(comment.getAuthor())) {
            return "failed";
        }

        boolean deleted = commentService.deleteComment(comment.getId());

        return deleted ? "success" : "failed";
    }
}
