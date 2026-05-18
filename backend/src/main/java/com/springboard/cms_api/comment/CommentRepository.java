package com.springboard.cms_api.comment;

import com.springboard.cms_api.comment.dto.CommentResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public CommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsById(Long id) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM comments
                    WHERE id = ? AND deleted_at IS NULL
                )""";
        Integer exists = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return exists != null && exists == 1;
    }

    public List<CommentResponse> findAllByPostId(Long id) {
        String sql = """
                SELECT
                    c.id,
                    c.post_id,
                    c.user_id,
                    u.display_name AS author_name,
                    c.content,
                    c.created_at
                FROM comments c
                JOIN posts p ON c.post_id = p.id
                JOIN users u ON c.user_id = u.id
                WHERE c.post_id = ?
                  AND p.deleted_at IS NULL
                  AND c.deleted_at IS NULL
                  AND u.deleted_at IS NULL
                ORDER BY c.id DESC
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> new CommentResponse(
                rs.getLong("id"),
                rs.getLong("post_id"),
                rs.getLong("user_id"),
                rs.getString("author_name"),
                rs.getString("content"),
                rs.getTimestamp("created_at").toLocalDateTime()
        ), id);
    }
}
