package com.pickandeat.authentication.infrastructure.database.redis;

import org.testcontainers.containers.GenericContainer;

public class SharedRedisContainer {
    private static final GenericContainer<?> REDIS_CONTAINER;

    static {
        REDIS_CONTAINER = new GenericContainer<>("docker.dragonflydb.io/dragonflydb/dragonfly").withExposedPorts(6379);
        REDIS_CONTAINER.start();
    }

    public static GenericContainer<?> getInstance() {
        return REDIS_CONTAINER;
    }
}
