package com.pickandeat.authentication.infrastructure;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.pickandeat.authentication.TestAuthenticationConfiguration;

@Testcontainers
@SpringBootTest(classes = TestAuthenticationConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractPostgresContainerTest {
    @DynamicPropertySource
    static void configureTestDatabase(DynamicPropertyRegistry registry) {
        PostgreSQLContainer<?> container = SharedPostgresContainer.getInstance();
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
}
