package com.springboard.cms_api.post;

import com.springboard.cms_api.post.dto.CreatePostRequest;
import com.springboard.cms_api.post.dto.UpdatePostRequest;
import com.springboard.cms_api.support.ControllerTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest extends ControllerTestSupport {

    private Long testPostId;
    private Long testUserId;
    @Autowired
    private ObjectMapper objectMapper;

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

        // 3. Write a post using that user's ID
        jdbcTemplate.update("""
            INSERT INTO posts (user_id, title, content)
            VALUES (?, ?, ?)
            """, testUserId, "Test Post", "Test Content");

        // 4. Get that post's ID -> testPostId
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

    @Test
    void createPost_withBlankTitle_returnsBadRequest() throws Exception {
        //given
        String url = "/api/posts";
        CreatePostRequest request = new CreatePostRequest(
                testUserId,
                "",
                "New Content"
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
    void createPost_withBlankContent_returnsBadRequest() throws Exception {
        // given
        String url = "/api/posts";
        CreatePostRequest request = new CreatePostRequest(
                testUserId, "", "New content"
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
    void createPost_withNullUserId_returnsBadRequest() throws Exception {
        // given
        String url = "/api/posts";
        CreatePostRequest request = new CreatePostRequest(
                null, "New title", "New content"
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
    void updatePost_returnsNoContent() throws Exception {
        // given
        String url = "/api/posts/{id}";
        UpdatePostRequest request = new UpdatePostRequest(
                "Updated title", "Updated content"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(patch(url, testPostId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    void updatePost_withBlankTitle_returnsBadRequest() throws Exception {
        // given
        String url = "/api/posts/{id}";
        UpdatePostRequest request = new UpdatePostRequest(
                "", "Updated content"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(patch(url, testPostId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void updatePost_withBlankContent_returnsBadRequest() throws Exception {
        // given
        String url = "/api/posts/{id}";
        UpdatePostRequest request = new UpdatePostRequest(
                "Updated title", ""
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(patch(url, testPostId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
    }
}