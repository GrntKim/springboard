package com.springboard.cms_api.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank
        @Size(max = 50)
        String loginId,

        @NotBlank
        @Size(min = 4, max = 255)
        String password,

        @NotBlank
        @Size(max = 50)
        String nickname
) {
}
