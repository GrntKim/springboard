package com.springboard.cms_api.user.dto;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String loginId,
        String nickname,
        LocalDateTime createdAt
) {
}
