package com.springboard.cms_api.auth;

import com.springboard.cms_api.auth.dto.CurrentUserResponse;
import com.springboard.cms_api.auth.dto.LoginRequest;
import com.springboard.cms_api.auth.dto.RegisterRequest;
import com.springboard.cms_api.support.ControllerTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTestSupport {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    private Long testUserId;
    private Long unknownTestUserId;
    private String testLoginId;
    private String testNickName;
    private String rawPassword;
    @Autowired
    private AuthService authService;

    @BeforeEach
    void setUp() {
        testLoginId = "auth_test_user";
        testNickName = "Auth Test User";
        rawPassword = "password1234";

        jdbcTemplate.update("""
            INSERT INTO users (login_id, password, nickname)
            VALUES (?, ?, ?)
            """,
                testLoginId,
                passwordEncoder.encode(rawPassword),
                testNickName
        );

        testUserId = jdbcTemplate.queryForObject("""
            SELECT id
            FROM users
            WHERE login_id = ?
            """, Long.class, testLoginId);

        unknownTestUserId = testUserId + 999L;
    }

    @Test
    void register_withValidRequest_returnsCreated() throws Exception {
        // given
        String url = "/api/auth/register";
        RegisterRequest request = new RegisterRequest(
                "new_auth_user",
                rawPassword,
                "test_nickname"
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
    void register_withDuplicateLoginId_returnsConflict() throws Exception {
        // given
        String url = "/api/auth/register";
        RegisterRequest request = new RegisterRequest(
                testLoginId,
                rawPassword,
                "test_nickname"
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
    void register_withBlankLoginId_returnsBadRequest() throws Exception {
        // given
        String url = "/api/auth/register";
        RegisterRequest request = new RegisterRequest(
                "",
                rawPassword,
                "test_nickname"
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
                "test_nickname"
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
                "test_nickname"
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
    void register_withBlankNickname_returnsBadRequest() throws Exception {
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

    @Test
    void login_withValidCredentials_returnsNoContent() throws Exception {
        // given
        String url = "/api/auth/login";
        LoginRequest request = new LoginRequest(
                testLoginId,
                rawPassword
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    void login_withUnknownLoginId_returnsUnauthorized() throws Exception {
        // given
        String url = "/api/auth/login";
        LoginRequest request = new LoginRequest(
                "Unknown_login_id",
                rawPassword
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    void login_withWrongPassword_returnsUnauthorized() throws Exception {
        // given
        String url = "/api/auth/login";
        LoginRequest request = new LoginRequest(
                testLoginId,
                "Unknown-password"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    void login_withBlankLoginId_returnsBadRequest() throws Exception {
        // given
        String url = "/api/auth/login";
        LoginRequest request = new LoginRequest(
                "",
                rawPassword
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
    void login_withBlankPassword_returnsBadRequest() throws Exception {
        // given
        String url = "/api/auth/login";
        LoginRequest request = new LoginRequest(
                testLoginId,
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

    @Test
    void me_withAuthenticatedSession_returnsCurrentUser() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("LOGIN_USER_ID", testUserId);

        // when
        ResultActions result = mockMvc.perform(get("/api/auth/me")
                .session(session));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUserId))
                .andExpect(jsonPath("$.loginId").value(testLoginId))
                .andExpect(jsonPath("$.nickname").value(testNickName));
    }

    @Test
    void me_withUnauthenticatedSession_returnsUnauthorized() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/api/auth/me"));

        // then
        result.andExpect(status().isUnauthorized());
    }
}
