package com.springboard.cms_api.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCommentRequest(
        @NotNull
        Long postId,

        @NotBlank
        String content
) {
}
