package com.pickandeat.authentication.infrastructure;

import org.testcontainers.containers.PostgreSQLContainer;

public class SharedPostgresContainer {
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER;

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName("pae-api")
                .withUsername("testuser")
                .withPassword("testpass")
                .withInitScript("db/migration/V1_Credentials_schema.sql");
        POSTGRES_CONTAINER.start();
    }

    public static PostgreSQLContainer<?> getInstance() {
        return POSTGRES_CONTAINER;
    }
}
