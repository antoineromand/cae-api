package com.pickandeat.authentication.infrastructure.database;

import com.pickandeat.authentication.TestConfiguration;
import com.pickandeat.authentication.infrastructure.database.postgres.SharedPostgresContainer;
import com.pickandeat.authentication.infrastructure.database.redis.SharedRedisContainer;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(classes = TestConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractDatabaseContainersTest {

  private static void configurePostgreSQLDatabase(DynamicPropertyRegistry registry) {
    if (SharedPostgresContainer.isEnabled()) {
      PostgreSQLContainer<?> container = SharedPostgresContainer.getInstance();
      registry.add("spring.datasource.url", container::getJdbcUrl);
      registry.add("spring.datasource.username", container::getUsername);
      registry.add("spring.datasource.password", container::getPassword);
      registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
      registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
      registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
      registry.add("spring.jpa.show-sql", () -> "true");
    } else {
      registry.add("spring.datasource.url", () -> "jdbc:postgresql://localhost:5432/pae-api");
      registry.add("spring.datasource.username", () -> "testuser");
      registry.add("spring.datasource.password", () -> "testpass");
      registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
      registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
      registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
      registry.add("spring.jpa.show-sql", () -> "true");
      System.out.println("[DEBUG] Using CI Postgres at localhost:5432");
    }
  }

  private static void configureRedisDatabase(DynamicPropertyRegistry registry) {
    if (SharedRedisContainer.isEnabled()) {
      GenericContainer<?> redis = SharedRedisContainer.getInstance();
      registry.add("spring.redis.host", redis::getHost);
      registry.add("spring.redis.port", () -> String.valueOf(redis.getMappedPort(6379)));
    } else {
      registry.add("spring.redis.host", () -> "localhost");
      registry.add("spring.redis.port", () -> "6379");
      System.out.println("[DEBUG] Using CI Redis at localhost:6379");
    }
  }

  @DynamicPropertySource
  static void configureSharedDatabases(DynamicPropertyRegistry registry) {
    configureRedisDatabase(registry);
    configurePostgreSQLDatabase(registry);
  }
}
