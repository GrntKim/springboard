package com.springboard.cms_api.user;

import com.springboard.cms_api.support.ControllerTestSupport;
import com.springboard.cms_api.user.dto.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTestSupport {

    private Long testUserId;
    private String testUserName;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // 1. Add a test user
        jdbcTemplate.update("""
            INSERT INTO users (username, password, display_name)
            VALUES (?, ?, ?)
            """, "post_test_user", "password", "Post Test User");

        // 2. Get that user's ID -> testUserId
        testUserId = jdbcTemplate.queryForObject("""
            SELECT id
            FROM users
            WHERE username = ?
            """, Long.class, "post_test_user");

        // 3. Get that user's name -> testUserName
        testUserName = jdbcTemplate.queryForObject("""
            SELECT username
            FROM users
            WHERE id = ?
            """, String.class, testUserId);
    }

    @Test
    void getUsers_returnsOk() throws Exception {
        // given
        String url = "/api/users";

        // when
        ResultActions result = mockMvc.perform(get(url));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getUser_returnsOk() throws Exception {
        // given
        String url = "/api/users/{id}";

        // when
        ResultActions result = mockMvc.perform(get(url, testUserId));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUserId));
    }

    @Test
    void getUser_withUnknownUserId_returnsNotFound() throws Exception {
        // given
        String url = "/api/users/{id}";
        Long unknownUserId = testUserId + 999L;

        // when
        ResultActions result = mockMvc.perform(get(url, unknownUserId));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void createUser_returnsOk() throws Exception {
        // given
        String url = "/api/users";
        CreateUserRequest request = new CreateUserRequest(
                "new_user_name",
                "password1234",
                "Test User"
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
    void createUser_withDuplicateUsername_returnsConflict() throws Exception {
        // given
        String url = "/api/users";
        CreateUserRequest request = new CreateUserRequest(
                testUserName, "password1234", "Test User"
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
    void createUser_withBlankUsername_returnsBadRequest() throws Exception {
        // given
        String url = "/api/users";
        CreateUserRequest request = new CreateUserRequest(
                "", "password1234", "Test User"
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
    void createUser_withBlankDisplayName_returnsBadRequest() throws Exception {
        // given
        String url = "/api/users";
        CreateUserRequest request = new CreateUserRequest(
                "test_create_user", "password1234", ""
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
    void createUser_withShortPassword_returnsBadRequest() throws Exception {
        // given
        String url = "/api/users";
        CreateUserRequest request = new CreateUserRequest(
                "test_create_user", "123", "Test User"
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