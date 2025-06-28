package com.pickandeat.authentication.infrastructure.database.redis;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class SharedRedisContainer {
    private static GenericContainer<?> instance;

    public static boolean isEnabled() {
        String env = System.getenv("USE_TESTCONTAINERS");
        return env == null || env.equalsIgnoreCase("true");
    }

    public static GenericContainer<?> getInstance() {
        if (instance == null && isEnabled()) {
            instance = new GenericContainer<>(DockerImageName.parse("redis:7.2")).withExposedPorts(6379);
            instance.start();
        }
        return instance;
    }

    public static boolean isRunning() {
        return instance != null && instance.isRunning();
    }
}
