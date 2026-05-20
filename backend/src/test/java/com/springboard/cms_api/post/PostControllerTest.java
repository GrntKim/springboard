package com.springboard.cms_api.post;

import com.springboard.cms_api.post.dto.CreatePostRequest;
import com.springboard.cms_api.post.dto.UpdatePostRequest;
import com.springboard.cms_api.support.ControllerTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest extends ControllerTestSupport {

    private Long testPostId;
    private Long testUserId;
    private Long unknownUserId;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // 1. Add a test user
        jdbcTemplate.update("""
            INSERT INTO users (login_id, password, nickname)
            VALUES (?, ?, ?)
            """, "post_test_user", "password", "Post Test User");

        // 2. Get that user's ID -> testUserId
        testUserId = jdbcTemplate.queryForObject("""
            SELECT id
            FROM users
            WHERE login_id = ?
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

        // 5. Set unknown User ID for testing edge cases
        unknownUserId = testUserId + 999L;
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
    void getPost_withUnknownId_returnsNotFound() throws Exception {
        // given
        String url = "/api/posts/{id}";
        Long unknownPostId = testPostId + 999L;

        // when
        ResultActions result = mockMvc.perform(get(url, unknownPostId));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void getPostsByUserId_returnsOk() throws Exception {
        // given
        String url = "/api/users/{userId}/posts";

        // when
        ResultActions result = mockMvc.perform(get(url, testUserId));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getPostsByUserId_withUnknownUserId_returnsNotFound() throws Exception {
        // given
        String url = "/api/users/{userId}/posts";

        // when
        ResultActions result = mockMvc.perform(get(url, unknownUserId));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void createPost_returnsCreated() throws Exception {
        // given
        String url = "/api/posts";
        CreatePostRequest request = new CreatePostRequest(
                "New Post",
                "New Content"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("LOGIN_USER_ID", testUserId);
        ResultActions result = mockMvc.perform(post(url)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());
    }

    @Test
    void createPost_withoutLogin_returnsUnauthorized() throws Exception {
        // given
        String url = "/api/posts";
        CreatePostRequest request = new CreatePostRequest(
                "New Post",
                "New Content"
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
    void updatePost_returnsNoContent() throws Exception {
        // given
        String url = "/api/posts/{id}";
        UpdatePostRequest request = new UpdatePostRequest(
                "Updated title", "Updated content"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put(url, testPostId)
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
        ResultActions result = mockMvc.perform(put(url, testPostId)
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
        ResultActions result = mockMvc.perform(put(url, testPostId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void updatePost_withUnknownPostId_returnsNotFound() throws Exception {
        // given
        String url = "/api/posts/{id}";
        Long unknownPostId = testPostId + 999L;
        UpdatePostRequest request = new UpdatePostRequest(
                "New title", "New content"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put(url, unknownPostId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void deletePost_returnsNoContent() throws Exception {
        // given
        String url = "/api/posts/{id}";

        // when
        ResultActions result = mockMvc.perform(delete(url, testPostId));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    void deletePost_withUnknownId_returnsNotFound() throws Exception {
        // given
        String url = "/api/posts/{id}";
        Long unknownPostId = testPostId + 999L;

        // when
        ResultActions result = mockMvc.perform(delete(url, unknownPostId));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void deletePost_excludesPostFromList() throws Exception {
        // given
        String deleteUrl = "/api/posts/{id}";
        mockMvc.perform(delete(deleteUrl, testPostId)).andExpect(status().isNoContent());
        String getUrl = "/api/posts";

        // when
        ResultActions result = mockMvc.perform(get(getUrl));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + testPostId + ")]").doesNotExist());
    }
}