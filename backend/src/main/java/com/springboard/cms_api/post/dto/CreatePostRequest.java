package com.springboard.cms_api.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreatePostRequest(
        @NotNull
        Long userId,

        @NotBlank
        @Size(max = 200)
        String title,

        @NotBlank
        String content
) {
}