package com.springboard.cms_api.comment.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long postId,
        Long userId,
        String authorName,
        String content,
        LocalDateTime createdAt
) {
}
