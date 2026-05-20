package com.springboard.cms_api.comment;

import com.springboard.cms_api.comment.dto.CommentResponse;
import com.springboard.cms_api.comment.dto.CreateCommentRequest;
import com.springboard.cms_api.comment.dto.UpdateCommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all comments")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment list retrieved")
    })
    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        List<CommentResponse> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Get comments by post id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment list retrieved"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentResponse> comments = commentService.getCommentsOfThePost(postId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Get comment by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment retrieved"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> getCommentByCommentId(@PathVariable Long commentId) {
        CommentResponse comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    @Operation(summary = "Create comment")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Comment created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Post or user not found")
    })
    @PostMapping("/comments")
    public ResponseEntity<Void> createComment(
            @Valid @RequestBody CreateCommentRequest request
    ) {
        commentService.createComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update comment")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Comment updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request
            ) {
        commentService.updateComment(commentId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete comment")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Comment deleted"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
