package com.pickandeat.authentication.infrastructure.database.redis;

import com.pickandeat.authentication.infrastructure.database.AbstractDatabaseContainersTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("integration")
public class RedisBasicIntegrationTest extends AbstractDatabaseContainersTest {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    void redisShouldRespond() {
        redisTemplate.opsForValue().set("x", "y");
        assertEquals("y", redisTemplate.opsForValue().get("x"));
    }
}
