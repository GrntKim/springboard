package com.springboard.cms_api.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private Long testPostId;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM comments");
        jdbcTemplate.update("DELETE FROM posts");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("ALTER TABLE comments AUTO_INCREMENT = 1");
        jdbcTemplate.update("ALTER TABLE posts AUTO_INCREMENT = 1");
        jdbcTemplate.update("ALTER TABLE users AUTO_INCREMENT = 1");

        jdbcTemplate.update("""
            INSERT INTO users (username, password, display_name)
            VALUES (?, ?, ?)
            """, "post_test_user", "password", "Post Test User");

        Long userId = jdbcTemplate.queryForObject("""
            SELECT id
            FROM users
            WHERE username = ?
            """, Long.class, "post_test_user");

        jdbcTemplate.update("""
            INSERT INTO posts (user_id, title, content)
            VALUES (?, ?, ?)
            """, userId, "Test Post", "Test Content");

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
}