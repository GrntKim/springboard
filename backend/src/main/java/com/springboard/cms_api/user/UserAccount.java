package com.springboard.cms_api.user;

public record UserAccount(
        Long id,
        String loginId,
        String password,
        String nickname
) {
}
