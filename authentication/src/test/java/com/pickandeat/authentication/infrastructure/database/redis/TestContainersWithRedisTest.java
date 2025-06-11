package com.pickandeat.authentication.infrastructure.database.redis;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.containers.GenericContainer;

import com.pickandeat.authentication.TestRedisConfiguration;

@SpringBootTest(classes = TestRedisConfiguration.class)
public class TestContainersWithRedisTest extends AbstractRedisContainerTest {
    @Test
    void shouldPingRedisDb() {
        GenericContainer<?> redisContainer = SharedRedisContainer.getInstance();

        assertTrue(redisContainer.isRunning());
    }

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Test
    void shouldWriteAndReadValue() {
        redisTemplate.opsForValue().set("foo", "bar");
        assertEquals("bar", redisTemplate.opsForValue().get("foo"));
    }
}
