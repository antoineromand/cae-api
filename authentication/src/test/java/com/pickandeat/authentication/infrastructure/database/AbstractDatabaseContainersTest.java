package com.pickandeat.authentication.infrastructure.database;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.pickandeat.authentication.TestConfiguration;
import com.pickandeat.authentication.infrastructure.database.postgres.SharedPostgresContainer;
import com.pickandeat.authentication.infrastructure.database.redis.SharedRedisContainer;

@Testcontainers
@SpringBootTest(classes = TestConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractDatabaseContainersTest {
    @DynamicPropertySource
    static void configurePostgreSQLDatabase(DynamicPropertyRegistry registry) {
        PostgreSQLContainer<?> container = SharedPostgresContainer.getInstance();
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.jpa.show-sql", () -> "true");
    }

    @DynamicPropertySource
    static void configureRedisDatabase(DynamicPropertyRegistry registry) {
        GenericContainer<?> redis = SharedRedisContainer.getInstance();
        System.setProperty("spring.redis.host", redis.getHost());
        System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString());
    }
}
