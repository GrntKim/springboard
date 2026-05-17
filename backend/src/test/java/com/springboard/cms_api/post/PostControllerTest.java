package com.springboard.cms_api.post;

import com.springboard.cms_api.post.dto.CreatePostRequest;
import com.springboard.cms_api.support.ControllerTestSupport;
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

class PostControllerTest extends ControllerTestSupport {

    private Long testPostId;
    private Long testUserId;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("""
            INSERT INTO users (username, password, display_name)
            VALUES (?, ?, ?)
            """, "post_test_user", "password", "Post Test User");

        testUserId = jdbcTemplate.queryForObject("""
            SELECT id
            FROM users
            WHERE username = ?
            """, Long.class, "post_test_user");

        jdbcTemplate.update("""
            INSERT INTO posts (user_id, title, content)
            VALUES (?, ?, ?)
            """, testUserId, "Test Post", "Test Content");

        testPostId = jdbcTemplate.queryForObject("""
            SELECT id
            FROM posts
            WHERE title = ?
            """, Long.class, "Test Post");
    }


    @Test
    void getPosts_returnsOk() throws Exception {
        // given
        String url = "/api/posts";

        // when
        ResultActions result = mockMvc.perform(get(url));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getPost_returnsOk() throws Exception {
        // given
        String url = "/api/posts/{id}";

        // when
        ResultActions result = mockMvc.perform(get(url, testPostId));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testPostId));
    }

    @Test
    void createPost_returnsCreated() throws Exception {
        // given
        String url = "/api/posts";
        CreatePostRequest request = new CreatePostRequest(
                testUserId,
                "New Post",
                "New Content"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());
    }
}