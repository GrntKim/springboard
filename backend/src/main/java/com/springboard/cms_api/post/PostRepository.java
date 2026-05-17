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
                WHERE p.id = ?
                """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new PostResponse(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getString("author_name"),
                rs.getTimestamp("created_at").toLocalDateTime()
        ), id);
    }
}
