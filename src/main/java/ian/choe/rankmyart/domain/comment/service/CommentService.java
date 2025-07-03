package ian.choe.rankmyart.domain.comment.service;

import ian.choe.rankmyart.domain.comment.repository.CommentRepository;
import ian.choe.rankmyart.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getCommentsByArtworkId(int artworkId) {
        return commentRepository.findCommentsByArtworkId(artworkId);
    }

    public void createComment(Comment comment) {
        commentRepository.save(comment);
    }

    public boolean deleteComment(int commentId) {
        return commentRepository.deleteById(commentId);
    }
}
