package com.springboard.cms_api.user;

import com.springboard.cms_api.user.dto.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    private static final String TEST_USERNAME = "test_create_user";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM comments");
        jdbcTemplate.update("DELETE FROM posts");
        jdbcTemplate.update("DELETE FROM users");

        jdbcTemplate.update("ALTER TABLE comments AUTO_INCREMENT = 1");
        jdbcTemplate.update("ALTER TABLE posts AUTO_INCREMENT = 1");
        jdbcTemplate.update("ALTER TABLE users AUTO_INCREMENT = 1");
    }

    @Test
    void getUsers_returnsOk() throws Exception {
        // given
        String url = "/api/users";

        jdbcTemplate.update("""
                INSERT INTO users (username, password, display_name)
                VALUES (?, ?, ?)
                """, TEST_USERNAME, "password", "User Test");

        // when
        ResultActions result = mockMvc.perform(get(url));

        // then
        result.andExpect(status().isOk());
    }

    @Test
    void createUser_returnsOk() throws Exception {
        // given
        String url = "/api/users";
        CreateUserRequest request = new CreateUserRequest(
                TEST_USERNAME,
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