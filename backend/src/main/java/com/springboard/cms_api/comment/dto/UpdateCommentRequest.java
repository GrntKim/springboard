package com.springboard.cms_api.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCommentRequest(
        @NotBlank
        String content
) {
}
