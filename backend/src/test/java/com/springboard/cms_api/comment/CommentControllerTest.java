package com.springboard.cms_api.comment;

import com.springboard.cms_api.comment.dto.CreateCommentRequest;
import com.springboard.cms_api.comment.dto.UpdateCommentRequest;
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

class CommentControllerTest extends ControllerTestSupport {

    @Autowired
    private ObjectMapper objectMapper;

    private Long postWriterId;
    private Long commentWriterId;

    private Long testCommentId;
    private Long testPostId;

    private Long unknownCommentId;
    private Long unknownPostId;
    private Long unknownUserId;

    @BeforeEach
    void setUp() {
        // 1. Add a post writer and comment writer
        jdbcTemplate.update("""
            INSERT INTO users (username, password, display_name)
            VALUES (?, ?, ?), (?, ?, ?)
            """, "post_writer", "password", "Post Writer",
                "comment_writer", "password1", "Comment Writer");

        // 2. Get post/comment writer's ID -> postWriterId, commentWriterId
        postWriterId = jdbcTemplate.queryForObject("""
            SELECT id
            FROM users
            WHERE username = ? AND deleted_at IS NULL
            """, Long.class, "post_writer");
        commentWriterId = jdbcTemplate.queryForObject("""
            SELECT id
            FROM users
            WHERE username = ? AND deleted_at IS NULL
            """, Long.class, "comment_writer");

        // 3. Write a post using that post writer's ID
        jdbcTemplate.update("""
            INSERT INTO posts (user_id, title, content)
            VALUES (?, ?, ?)
            """, postWriterId, "Test Post", "Test Content");

        // 4. Get that post's ID -> testPostId
        testPostId = jdbcTemplate.queryForObject("""
            SELECT id
            FROM posts
            WHERE title = ? AND deleted_at IS NULL
            """, Long.class, "Test Post");

        // 5. Write a comment with comment writer's ID and post's ID
        jdbcTemplate.update("""
            INSERT INTO comments (post_id, user_id, content)
            VALUES (?, ?, ?)
            """, testPostId, commentWriterId, "comment_content");

        // 6. Get that comment's ID using comment writer's ID and post's ID-> testCommentId
        testCommentId = jdbcTemplate.queryForObject("""
            SELECT id
            FROM comments
            WHERE post_id = ? AND user_id = ? AND deleted_at IS NULL
            """, Long.class, testPostId, commentWriterId);

        // 7. setup unknown IDs
        unknownCommentId = testCommentId + 999L;
        unknownPostId = testPostId + 999L;
        unknownUserId = postWriterId + 999L;
    }

    @Test
    void getCommentsByPostId_returnsOk() throws Exception {
        // given
        String url = "/api/posts/{postId}/comments";

        // when
        ResultActions result = mockMvc.perform(get(url, testPostId));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getCommentsByPostId_withUnknownPostId_returnsNotFound() throws Exception {
        // given
        String url = "/api/posts/{postId}/comments";

        // when
        ResultActions result = mockMvc.perform(get(url, unknownPostId));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void getCommentByCommentId_returnsOk() throws Exception {
        // given
        String url = "/api/comments/{commentId}";

        // when
        ResultActions result = mockMvc.perform(get(url, testCommentId));

        // then
        result.andExpect(status().isOk());
    }

    @Test
    void getCommentByCommentId_withUnknownCommentId_returnsNotFound() throws Exception {
        // given
        String url = "/api/comments/{commentId}";

        // when
        ResultActions result = mockMvc.perform(get(url, unknownCommentId));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void createComment_returnsCreated() throws Exception {
        // given
        String url = "/api/comments";
        CreateCommentRequest request = new CreateCommentRequest(
                testPostId,
                commentWriterId,
                "Comment content"
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
    void createComment_withBlankTitle_returnsBadRequest() throws Exception {
        // given
        String url = "/api/comments";
        CreateCommentRequest request = new CreateCommentRequest(
                testPostId,
                commentWriterId,
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
    void createComment_withNullPostId_returnsBadRequest() throws Exception {
        // given
        String url = "/api/comments";
        CreateCommentRequest request = new CreateCommentRequest(
                null,
                commentWriterId,
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
    void createComment_withUnknownPostId_returnsNotFound() throws Exception {
        // given
        String url = "/api/comments";
        CreateCommentRequest request = new CreateCommentRequest(
                unknownPostId,
                commentWriterId,
                "Comment content"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void createComment_withNullUserId_returnsBadRequest() throws Exception {
        // given
        String url = "/api/comments";
        CreateCommentRequest request = new CreateCommentRequest(
                testPostId,
                null,
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
    void createComment_withUnknownUserId_returnsNotFound() throws Exception {
        // given
        String url = "/api/comments";
        CreateCommentRequest request = new CreateCommentRequest(
                testPostId,
                unknownUserId,
                "Comment content"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void updateComment_returnsNoContent() throws Exception {
        // given
        String url = "/api/comments/{commentId}";
        UpdateCommentRequest request = new UpdateCommentRequest("Updated Comment content");
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put(url, testCommentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isNoContent());
    }

    @Test
    void updateComment_withUnknownCommentId_returnsNotFound() throws Exception {
        // given
        String url = "/api/comments/{commentId}";
        UpdateCommentRequest request = new UpdateCommentRequest("Updated Comment content");
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put(url, unknownCommentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    void updateComment_blankContent_returnsBadRequest() throws Exception {
        // given
        String url = "/api/comments/{commentId}";
        UpdateCommentRequest request = new UpdateCommentRequest("");
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(put(url, testCommentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        result.andExpect(status().isBadRequest());
    }
}