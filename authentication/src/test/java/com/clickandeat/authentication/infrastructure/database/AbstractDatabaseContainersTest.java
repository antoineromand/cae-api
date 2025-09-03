package com.clickandeat.authentication.infrastructure.database;

import com.clickandeat.authentication.TestConfiguration;
import com.clickandeat.shared.config.test.SharedPostgresContainer;
import com.clickandeat.shared.config.test.SharedRedisContainer;
import com.redis.testcontainers.RedisContainer;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
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

      Flyway.configure()
          .dataSource(container.getJdbcUrl(), container.getUsername(), container.getPassword())
          .locations("classpath:db/migration")
          .load()
          .migrate();
    } else {
      registry.add("spring.datasource.url", () -> "jdbc:postgresql://localhost:5432/pae-api");
      registry.add("spring.datasource.username", () -> "testuser");
      registry.add("spring.datasource.password", () -> "testpass");
      registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
      registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
      registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
      registry.add("spring.jpa.show-sql", () -> "true");
    }
  }

  private static void configureRedisDatabase(DynamicPropertyRegistry registry) {
    System.out.println(registry.toString());
    if (SharedRedisContainer.isEnabled()) {
      RedisContainer redis = SharedRedisContainer.getInstance();
      registry.add("spring.data.redis.host", redis::getHost);
      registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    } else {
      registry.add("spring.data.redis.host", () -> "localhost");
      registry.add("spring.data.redis.port", () -> "6379");
    }
  }

  @DynamicPropertySource
  static void configureSharedDatabases(DynamicPropertyRegistry registry) {
    configureRedisDatabase(registry);
    configurePostgreSQLDatabase(registry);
  }
}
