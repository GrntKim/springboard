package com.springboard.cms_api.user;

import com.springboard.cms_api.support.ControllerTestSupport;
import com.springboard.cms_api.user.dto.CreateUserRequest;
import com.springboard.cms_api.user.dto.UpdateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTestSupport {

    private Long testUserId;
    private Long unknownUserId;
    private String testUserName;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // 1. Add a test user
        jdbcTemplate.update("""
            INSERT INTO users (username, password, display_name)
            VALUES (?, ?, ?), (?, ?, ?)
            """, "post_test_user", "password", "Post Test User",
                "already_existing_user", "password1", "Existing user");

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

        // 4. Setup unknown user ID
        unknownUserId = testUserId + 999L;
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

    @Test
    void updateUser_returnsOk() throws Exception {
        // given
        String url = "/api/users/{id}";
        UpdateUserRequest request = new UpdateUserRequest(
                "new_user_name",
                "new_password",
                "new_display_name"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put(url, testUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    void updateUser_withUnknownUserId_returnsNotFound() throws Exception {
        // given
        String url = "/api/users/{id}";
        UpdateUserRequest request = new UpdateUserRequest(
                "new_user_name",
                "new_password",
                "new_display_name"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put(url, unknownUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void updateUser_withDuplicateUserName_returnsConflict() throws Exception {
        // given
        String url = "/api/users/{id}";
        UpdateUserRequest request = new UpdateUserRequest(
                "already_existing_user",
                "new_password",
                "new_display_name"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put(url, testUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isConflict());
    }

    @Test
    void updateUser_withBlankUserName_returnsBadRequest() throws Exception {
        // given
        String url = "/api/users/{id}";
        UpdateUserRequest request = new UpdateUserRequest(
                "",
                "new_password",
                "new_display_name"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put(url, testUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_withBlankPassword_returnsBadRequest() throws Exception {
        // given
        String url = "/api/users/{id}";
        UpdateUserRequest request = new UpdateUserRequest(
                "new_username",
                "",
                "new_display_name"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put(url, testUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_withBlankDisplayName_returnsBadRequest() throws Exception {
        // given
        String url = "/api/users/{id}";
        UpdateUserRequest request = new UpdateUserRequest(
                "new_username",
                "new_password",
                ""
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put(url, testUserId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_returnsNoContent() throws Exception {
        // given
        String url = "/api/users/{id}";

        // when
        ResultActions result = mockMvc.perform(delete(url, testUserId));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_withUnknownUserID_returnsNotFound() throws Exception {
        // given
        String url = "/api/users/{id}";

        // when
        ResultActions result = mockMvc.perform(delete(url, unknownUserId));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_excludesUserFromList() throws Exception {
        // given
        String deleteUrl = "/api/users/{id}";
        mockMvc.perform(delete(deleteUrl, testUserId)).andExpect(status().isNoContent());
        String getUrl = "/api/users";

        // when
        ResultActions result = mockMvc.perform(get(getUrl));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + testUserId + ")]").doesNotExist());
    }
}