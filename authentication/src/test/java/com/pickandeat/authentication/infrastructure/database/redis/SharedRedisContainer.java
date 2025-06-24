package com.pickandeat.authentication.infrastructure.database.redis;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.time.Duration;

public class SharedRedisContainer {
    private static final GenericContainer<?> REDIS_CONTAINER =
            new GenericContainer<>("redis:7")
                    .withExposedPorts(6379)
                    .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));

    static {
        REDIS_CONTAINER.start();
    }

    public static GenericContainer<?> getInstance() {
        if (!REDIS_CONTAINER.isRunning()) {
            REDIS_CONTAINER.start();
        }
        return REDIS_CONTAINER;
    }
}
