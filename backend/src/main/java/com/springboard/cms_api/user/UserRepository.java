package com.springboard.cms_api.user;

import com.springboard.cms_api.user.dto.UserResponse;
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
                SELECT id, username, display_name, created_at
                FROM users
                ORDER BY id DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new UserResponse(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("display_name"),
                rs.getTimestamp("created_at").toLocalDateTime()
        ));
    }

    public boolean existsByUsername(String username) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1
                    FROM users
                    WHERE username = ?
                )""";

        Integer exists = jdbcTemplate.queryForObject(sql, Integer.class, username);

        return exists != null && exists == 1;
    }

    public void save(String username, String password, String displayName) {
        String sql = """
                INSERT INTO users (username, password, display_name)
                VALUES (?, ?, ?)
                """;

        jdbcTemplate.update(sql, username, password, displayName);
    }
}
