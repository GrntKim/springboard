package com.springboard.cms_api.user;

import com.springboard.cms_api.user.dto.UpdateUserRequest;
import com.springboard.cms_api.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<UserResponse> findAll() {
        String sql = """
                SELECT id, login_id, nickname, created_at
                FROM users
                WHERE deleted_at IS NULL
                ORDER BY id DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new UserResponse(
                rs.getLong("id"),
                rs.getString("login_id"),
                rs.getString("nickname"),
                rs.getTimestamp("created_at").toLocalDateTime()
        ));
    }

    public UserResponse findById(Long id) {
        String sql = """
                SELECT id, login_id, nickname, created_at
                FROM users
                WHERE id = ? AND deleted_at IS NULL
                """;
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new UserResponse(
                rs.getLong("id"),
                rs.getString("login_id"),
                rs.getString("nickname"),
                rs.getTimestamp("created_at").toLocalDateTime()
        ), id);
    }

    public boolean existsByLoginId(String loginId) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1
                    FROM users
                    WHERE login_id = ? AND deleted_at IS NULL
                )""";

        Integer exists = jdbcTemplate.queryForObject(sql, Integer.class, loginId);

        return exists != null && exists == 1;
    }

    public boolean existsById(Long id) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1
                    FROM users
                    WHERE id = ? AND deleted_at IS NULL
                )""";

        Integer exists = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return exists != null && exists == 1;
    }

    public void save(String loginId, String password, String nickname) {
        String sql = """
                INSERT INTO users (login_id, password, nickname)
                VALUES (?, ?, ?)
                """;

        jdbcTemplate.update(sql, loginId, password, nickname);
    }

    public void update(Long id, String loginId, String password, String nickname) {
        String sql = """
                UPDATE users
                SET login_id = ?,
                    password = ?,
                    nickname = ?,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, loginId, password, nickname, id);
    }

    public void delete(Long id) {
        String sql = """
                UPDATE users
                SET deleted_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }
}
