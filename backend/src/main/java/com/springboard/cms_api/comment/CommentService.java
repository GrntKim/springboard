package com.springboard.cms_api.comment;

import com.springboard.cms_api.auth.AuthService;
import com.springboard.cms_api.comment.dto.CommentResponse;
import com.springboard.cms_api.comment.dto.CreateCommentRequest;
import com.springboard.cms_api.comment.dto.UpdateCommentRequest;
import com.springboard.cms_api.post.PostRepository;
import com.springboard.cms_api.post.dto.PostResponse;
import com.springboard.cms_api.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository, AuthService authService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public void validateCommentIdExists(Long id) {
        if(!commentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }
    }

    public void validatePostIdExists(Long id) {
        if(!postRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
        }
    }

    public void validateUserIdExists(Long id) {
        if(!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    private void validateCommentOwner(Long commentId, Long loginUserId) {
        CommentResponse comment = commentRepository.findById(commentId);
        if (!comment.userId().equals(loginUserId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Forbidden"
            );
        }
    }

    public List<CommentResponse> getAllComments() {
        return commentRepository.findAll();
    }

    public List<CommentResponse> getCommentsOfThePost(Long postId) {
        validatePostIdExists(postId);
        return commentRepository.findAllByPostId(postId);
    }

    public CommentResponse getCommentById(Long commentId) {
        validateCommentIdExists(commentId);
        return commentRepository.findById(commentId);
    }

    public void createComment(
            @Valid CreateCommentRequest request,
            HttpSession session
    ) {
        Long userId = authService.getLoginUserId(session);
        validatePostIdExists(request.postId());

        commentRepository.save(
                request.postId(),
                userId,
                request.content()
        );
    }

    public void updateComment(
            Long commentId,
            @Valid UpdateCommentRequest request,
            HttpSession session
    ) {
        validateCommentIdExists(commentId);
        validateCommentOwner(commentId, authService.getLoginUserId(session));
        commentRepository.update(commentId, request.content());
    }

    public void deleteComment(
            Long commentId,
            HttpSession session
    ) {
        validateCommentIdExists(commentId);
        validateCommentOwner(commentId, authService.getLoginUserId(session));
        commentRepository.delete(commentId);
    }

}
