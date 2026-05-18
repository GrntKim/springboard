package com.springboard.cms_api.comment;

import com.springboard.cms_api.comment.dto.CommentResponse;
import com.springboard.cms_api.post.PostRepository;
import com.springboard.cms_api.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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

    public List<CommentResponse> getCommentsOfThePost(Long postId) {
        validatePostIdExists(postId);
        return commentRepository.findAllByPostId(postId);
    }

    public CommentResponse getCommentById(Long commentId) {
        validateCommentIdExists(commentId);
        return commentRepository.findById(commentId);
    }
}
