package com.pickandeat.authentication.infrastructure.database.postgres;

import org.testcontainers.containers.PostgreSQLContainer;

public class SharedPostgresContainer {
    private static PostgreSQLContainer<?> INSTANCE;

    public static boolean isEnabled() {
        String env = System.getenv("USE_TESTCONTAINERS");
        return env == null || env.equalsIgnoreCase("true");
    }

    public static PostgreSQLContainer<?> getInstance() {
        if (INSTANCE == null && isEnabled()) {
            INSTANCE =
                    new PostgreSQLContainer<>("postgres:15")
                            .withDatabaseName("pae-api")
                            .withUsername("testuser")
                            .withPassword("testpass")
                            .withInitScript("db/migration/V1_Credentials_schema.sql");
            INSTANCE.start();
        }
        return INSTANCE;
    }

    public static boolean isRunning() {
        return INSTANCE != null && INSTANCE.isRunning();
    }
}
