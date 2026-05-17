package com.springboard.cms_api.health;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbHealthController {

    private final JdbcTemplate jdbcTemplate;

    public DbHealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/db-health")
    public ResponseEntity<String> checkHealth() {
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);

        if (result != null && result == 1) {
            return ResponseEntity.ok("Database is running healthy!");
        }

        return ResponseEntity.internalServerError().body("Database health check failed.");
    }
}
