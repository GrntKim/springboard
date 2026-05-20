package com.springboard.cms_api.auth.dto;

public record CurrentUserResponse(
        Long id,
        String loginId,
        String nickname
) {
}
