package com.pickandeat.authentication.infrastructure.database.redis;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.pickandeat.authentication.TestRedisConfiguration;

@Testcontainers
@SpringBootTest(classes = TestRedisConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractRedisContainerTest {
    @DynamicPropertySource
    static void configureRedisTestDatabase(DynamicPropertyRegistry registry) {
        GenericContainer<?> redis = SharedRedisContainer.getInstance();
        redis.start();
        System.setProperty("spring.redis.host", redis.getHost());
        System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString());
    }
}
