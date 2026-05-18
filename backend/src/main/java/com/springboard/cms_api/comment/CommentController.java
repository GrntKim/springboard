package com.springboard.cms_api.comment;

import com.springboard.cms_api.comment.dto.CommentResponse;
import com.springboard.cms_api.comment.dto.CreateCommentRequest;
import com.springboard.cms_api.comment.dto.UpdateCommentRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/comments")
    public ResponseEntity<Void> createComment(
            @Valid @RequestBody CreateCommentRequest request
            ) {
        commentService.createComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request
            ) {
        commentService.updateComment(commentId, request);
        return ResponseEntity.noContent().build();
    }
}
