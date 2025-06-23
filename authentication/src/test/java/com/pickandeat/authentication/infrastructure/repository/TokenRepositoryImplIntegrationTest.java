package com.pickandeat.authentication.infrastructure.repository;

import com.pickandeat.authentication.TestConfiguration;
import com.pickandeat.authentication.infrastructure.database.AbstractDatabaseContainersTest;
import com.pickandeat.authentication.infrastructure.repository.cache.TokenRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.UUID;

@SpringBootTest(classes = TestConfiguration.class)
public class TokenRepositoryImplIntegrationTest extends AbstractDatabaseContainersTest {
    @Autowired
    TokenRepositoryImpl tokenRepositoryImpl;

    @Test
    void storeRefreshToken_shouldSaveUserId_whenJtiAndDurationProvided() throws InterruptedException {
        String jti = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        Duration duration = Duration.ofMinutes(5);

        tokenRepositoryImpl.storeRefreshToken(jti, userId, duration);

        Thread.sleep(3000);

        Assertions.assertEquals(userId, tokenRepositoryImpl.getUserIdByJti(jti));

    }

    @Test
    void getUserIdByJti_shouldReturnNull_whenTokenIsExpired() throws InterruptedException {
        String jti = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        Duration duration = Duration.ofSeconds(2);

        tokenRepositoryImpl.storeRefreshToken(jti, userId, duration);

        Assertions.assertEquals(userId, tokenRepositoryImpl.getUserIdByJti(jti));

        Thread.sleep(2500);

        Assertions.assertNull(tokenRepositoryImpl.getUserIdByJti(jti));
    }

    @Test
    void deleteByJti_shouldDeleteToken_whenJtiExists() throws InterruptedException {
        String jti = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        Duration duration = Duration.ofMinutes(5);

        tokenRepositoryImpl.storeRefreshToken(jti, userId, duration);

        tokenRepositoryImpl.deleteByJti(jti);

        Assertions.assertNull(tokenRepositoryImpl.getUserIdByJti(jti));
    }



}
