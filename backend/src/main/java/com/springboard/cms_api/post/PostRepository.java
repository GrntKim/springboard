package com.springboard.cms_api.post;

import com.springboard.cms_api.post.dto.PostResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepository {

    private final JdbcTemplate jdbcTemplate;


    public PostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PostResponse> findAll() {
        String sql = """
                SELECT
                    p.id,
                    p.title,
                    p.content,
                    u.display_name AS author_name,
                    p.created_at
                FROM posts p
                JOIN users u ON p.user_id = u.id
                WHERE p.deleted_at IS NULL
                ORDER BY p.id DESC
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new PostResponse(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getString("author_name"),
                rs.getTimestamp("created_at").toLocalDateTime()
        ));
    }

    public PostResponse findById(Long id) {
        String sql = """
                SELECT
                    p.id,
                    p.title,
                    p.content,
                    u.display_name AS author_name,
                    p.created_at
                FROM posts p
                JOIN users u ON p.user_id = u.id
                WHERE p.id = ? AND p.deleted_at IS NULL
                """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new PostResponse(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getString("author_name"),
                rs.getTimestamp("created_at").toLocalDateTime()
        ), id);
    }

    public void save(Long userId, String title, String content) {
        String sql = """
                INSERT INTO posts (user_id, title, content)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(sql, userId, title, content);
    }

    public void update(Long postId, String title, String content) {
        String sql = """
                UPDATE posts
                SET title = ?, content = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, title, content, postId);
    }

    public void delete(Long postId) {
        String sql = """
                UPDATE posts
                SET deleted_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, postId);
    }

    public boolean existsById(Long postId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM posts
                    WHERE id = ? AND deleted_at IS NULL
                )""";
        Integer exists = jdbcTemplate.queryForObject(sql, Integer.class, postId);
        return exists != null && exists == 1;
    }
}
