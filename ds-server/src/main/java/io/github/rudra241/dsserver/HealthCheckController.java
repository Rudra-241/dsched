package io.github.rudra241.dsserver;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class HealthCheckController {

    private final StringRedisTemplate redisTemplate;
    private final JdbcTemplate jdbcTemplate;

    public HealthCheckController(StringRedisTemplate redisTemplate, JdbcTemplate jdbcTemplate) {
        this.redisTemplate = redisTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/status")
    public Map<String, String> getStatus() {
        Map<String, String> status = new HashMap<>();

        // for Redis
        try {
            redisTemplate.opsForValue().set("health-check", "up", Duration.ofSeconds(10));
            status.put("redis", "OK: " + redisTemplate.opsForValue().get("health-check"));
        } catch (Exception e) {
            status.put("redis", "FAILED: " + e.getMessage());
        }

        // for Postgres
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            status.put("postgres", result == 1 ? "OK" : "FAILED");
        } catch (Exception e) {
            status.put("postgres", "FAILED: " + e.getMessage());
        }

        status.put("java_version", System.getProperty("java.version"));
        return status;
    }
}