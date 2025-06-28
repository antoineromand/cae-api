package com.pickandeat.shared.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
public class TokenProviderUnitTest {

    private static final String SECRET = "YmFzZTY0ZW5jb2RlZHNlY3JldGtleUhlcmVXaXRoMzJieXRlcw=="; // base64 32-byte key
    private static final long ACCESS_EXPIRATION_MS = 3600;

    private TokenProvider tokenProvider;
    private TokenPayload testPayload;

    @BeforeEach
    void setUp() {
        tokenProvider = new TokenProvider(SECRET, ACCESS_EXPIRATION_MS);
        testPayload = new TokenPayload(UUID.randomUUID(), "USER");
    }

    @Test
    void generateAccessToken_shouldReturnValidToken() {
        String token = tokenProvider.generateAccessToken(testPayload);
        assertNotNull(token);
        assertTrue(tokenProvider.verifyAccessToken(token));
    }

    @Test
    void generateRefreshToken_shouldReturnValidToken() {
        String token = tokenProvider.generateRefreshToken(testPayload, Duration.ofMinutes(5));
        assertNotNull(token);
        assertTrue(tokenProvider.verifyRefreshToken(token));
    }

    @Test
    void verifyAccessToken_shouldReturnFalseForInvalidToken() {
        assertFalse(tokenProvider.verifyAccessToken("invalid.token.value"));
    }

    @Test
    void decodeAccessToken_shouldReturnCorrectPayload() {
        String token = tokenProvider.generateAccessToken(testPayload);
        TokenPayload payload = tokenProvider.decodeAccessToken(token);

        assertEquals(testPayload.getUserId(), payload.getUserId());
        assertEquals(testPayload.getRole(), payload.getRole());
    }

    @Test
    void extractJtiFromToken_shouldReturnJti() {
        String token = tokenProvider.generateAccessToken(testPayload);
        String jti = tokenProvider.extractJtiFromToken(token);

        assertNotNull(jti);
        assertFalse(jti.isEmpty());
    }

    @Test
    void extractExpirationFromToken_shouldReturnFutureDate() {
        String token = tokenProvider.generateAccessToken(testPayload);
        Date expiration = tokenProvider.extractExpirationFromToken(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void validate_shouldReturnFalseForExpiredToken() {
        TokenProvider shortLivedProvider = new TokenProvider(SECRET, 1); // expires in 1 second
        String token = shortLivedProvider.generateAccessToken(testPayload);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertFalse(shortLivedProvider.verifyAccessToken(token));
    }

    @Test
    void validate_shouldReturnFalseForNullOrEmptyToken() {
        assertFalse(tokenProvider.verifyAccessToken(null));
        assertFalse(tokenProvider.verifyAccessToken(""));
    }
}
