package com.pickandeat.shared.token;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pickandeat.shared.token.domain.TokenPayload;
import com.pickandeat.shared.token.infrastructure.TokenProvider;

class TokenProviderTest {

    private TokenProvider tokenProvider;
    private final String secret = java.util.Base64.getEncoder()
            .encodeToString("my-super-secret-key-which-is-long-enough".getBytes());
    private final long accessExpiration = 6000;
    private final long refreshExpiration = 12000;
    private UUID userId;
    private String role;

    @BeforeEach
    void setUp() {
        tokenProvider = new TokenProvider(secret, accessExpiration, refreshExpiration);
        userId = UUID.randomUUID();
        role = "ROLE_USER";
    }

    @Test
    void generateAccessToken_shouldBeValid() {
        String token = tokenProvider.generateAccessToken(new TokenPayload(userId, role));
        assertNotNull(token);
        assertTrue(tokenProvider.verifyAccessToken(token));
    }

    @Test
    void generateRefreshToken_shouldBeValid() {
        String token = tokenProvider.generateRefreshToken(new TokenPayload(userId, role));
        assertNotNull(token);
        assertTrue(tokenProvider.verifyRefreshToken(token));
    }

    @Test
    void decodeAccessToken_shouldReturnCorrectPayload() {
        String token = tokenProvider.generateAccessToken(new TokenPayload(userId, role));
        TokenPayload payload = tokenProvider.decodeAccessToken(token);

        assertEquals(userId, payload.getUserId());
        assertEquals(role, payload.getRole());
    }

    @Test
    void verifyToken_shouldReturnFalseForInvalidToken() {
        assertFalse(tokenProvider.verifyAccessToken("invalid.token.here"));
    }

    @Test
    void decodeAccessToken_shouldThrowForMalformedToken() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            tokenProvider.decodeAccessToken("invalid.token.structure");
        });
        assertTrue(exception.getMessage().contains("Error while decoding JWT"));
    }

    @Test
    void verifyToken_shouldReturnFalseForExpiredToken() throws InterruptedException {
        TokenProvider shortLivedProvider = new TokenProvider(secret, 1, 1);
        String token = shortLivedProvider.generateAccessToken(new TokenPayload(userId, role));
        Thread.sleep(1500);
        assertFalse(shortLivedProvider.verifyAccessToken(token));
    }
}
