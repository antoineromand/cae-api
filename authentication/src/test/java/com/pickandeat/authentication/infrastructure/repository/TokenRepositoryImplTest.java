package com.pickandeat.authentication.infrastructure.repository;

import java.time.Duration;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pickandeat.authentication.TestConfiguration;
import com.pickandeat.authentication.infrastructure.database.AbstractDatabaseContainersTest;
import com.pickandeat.authentication.infrastructure.repository.cache.TokenRepositoryImpl;

@SpringBootTest(classes = TestConfiguration.class)
public class TokenRepositoryImplTest extends AbstractDatabaseContainersTest {
    @Autowired
    TokenRepositoryImpl tokenRepositoryImpl;

    @Test
    void shouldInsertUserId() throws InterruptedException {
        String jti = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        Duration duration = Duration.ofMinutes(5);

        tokenRepositoryImpl.storeRefreshToken(jti, userId, duration);

        Thread.sleep(3000);

        assertEquals(userId, tokenRepositoryImpl.getUserIdByJti(jti));

    }

    @Test
    void shouldExpireTokenAfterTTL() throws InterruptedException {
        String jti = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        Duration duration = Duration.ofSeconds(2);

        tokenRepositoryImpl.storeRefreshToken(jti, userId, duration);

        assertEquals(userId, tokenRepositoryImpl.getUserIdByJti(jti));

        Thread.sleep(2500);

        assertEquals(null, tokenRepositoryImpl.getUserIdByJti(jti));
    }

}
