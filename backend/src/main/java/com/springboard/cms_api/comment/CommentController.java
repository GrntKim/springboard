package com.springboard.cms_api.comment;

import com.springboard.cms_api.comment.dto.CommentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/posts/{postId}/comments")
    public  ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentResponse> comments = commentService.getCommentsOfThePost(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> getCommentByCommentId(@PathVariable Long commentId) {
        CommentResponse comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }
}
