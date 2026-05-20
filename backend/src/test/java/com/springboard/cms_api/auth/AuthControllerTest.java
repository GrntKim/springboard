package com.springboard.cms_api.auth;

import com.springboard.cms_api.auth.dto.RegisterRequest;
import com.springboard.cms_api.support.ControllerTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTestSupport {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    private Long testUserId;
    private String testUsername;
    private String rawPassword;

    @BeforeEach
    void setUp() {
        testUsername = "auth_test_user";
        rawPassword = "password1234";

        jdbcTemplate.update("""
            INSERT INTO users (username, password, display_name)
            VALUES (?, ?, ?)
            """,
                testUsername,
                passwordEncoder.encode(rawPassword),
                "Auth Test User"
        );

        testUserId = jdbcTemplate.queryForObject("""
            SELECT id
            FROM users
            WHERE username = ?
            """, Long.class, testUsername);
    }

    @Test
    void register_withValidRequest_returnsCreated() throws Exception {
        // given
        String url = "/api/auth/register";
        RegisterRequest request = new RegisterRequest(
                "new_auth_user",
                rawPassword,
                "test_display_name"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());
    }

    @Test
    void register_withDuplicateUsername_returnsConflict() throws Exception {
        // given
        String url = "/api/auth/register";
        RegisterRequest request = new RegisterRequest(
                testUsername,
                rawPassword,
                "test_display_name"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isConflict());
    }

    @Test
    void register_withBlankUsername_returnsBadRequest() throws Exception {
        // given
        String url = "/api/auth/register";
        RegisterRequest request = new RegisterRequest(
                "",
                rawPassword,
                "test_display_name"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void register_withBlankPassword_returnsBadRequest() throws Exception {
        // given
        String url = "/api/auth/register";
        RegisterRequest request = new RegisterRequest(
                "new_auth_user",
                "",
                "test_display_name"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void register_withShortPassword_returnsBadRequest() throws Exception {
        // given
        String url = "/api/auth/register";
        RegisterRequest request = new RegisterRequest(
                "new_auth_user",
                "123",
                "test_display_name"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void register_withBlankDisplayName_returnsBadRequest() throws Exception {
        // given
        String url = "/api/auth/register";
        RegisterRequest request = new RegisterRequest(
                "new_auth_user",
                rawPassword,
                ""
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
    }
}