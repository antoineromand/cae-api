package com.clickandeat.shared.config.test;

import com.redis.testcontainers.RedisContainer;

public class SharedRedisContainer {
    private static RedisContainer instance;

    public static boolean isEnabled() {
        String env = System.getenv("USE_TESTCONTAINERS");
        return env == null || env.equalsIgnoreCase("true");
    }

    public static RedisContainer getInstance() {
        if (instance == null && isEnabled()) {
            instance = new RedisContainer("redis:7.2.3-alpine");
            instance.withExposedPorts(6379);
            instance.start();
        }
        return instance;
    }

    public static boolean isRunning() {
        return instance != null && instance.isRunning();
    }
}
