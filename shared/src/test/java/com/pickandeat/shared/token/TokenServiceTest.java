package com.pickandeat.shared.token;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pickandeat.shared.token.application.TokenService;
import com.pickandeat.shared.token.domain.ITokenProvider;
import com.pickandeat.shared.token.domain.TokenPayload;

class TokenServiceTest {

    private ITokenProvider tokenProvider;
    private TokenService tokenService;
    private UUID userId;
    private String role;

    @BeforeEach
    void setUp() {
        tokenProvider = mock(ITokenProvider.class);
        tokenService = new TokenService(tokenProvider);
        userId = UUID.randomUUID();
        role = "ROLE_USER";
    }

    @Test
    void createAccessToken_shouldDelegateToProvider() {
        String expectedToken = "access.token";
        when(tokenProvider.generateAccessToken(any())).thenReturn(expectedToken);

        String token = tokenService.createAccessToken(userId, role);
        assertEquals(expectedToken, token);
        verify(tokenProvider).generateAccessToken(any());
    }

    @Test
    void isAccessTokenValid_shouldCallProvider() {
        when(tokenProvider.verifyAccessToken("token")).thenReturn(true);
        assertTrue(tokenService.isAccessTokenValid("token"));
    }

    @Test
    void extractPayload_shouldReturnPayloadFromProvider() {
        TokenPayload expected = new TokenPayload(userId, role);
        when(tokenProvider.decodeAccessToken("token")).thenReturn(expected);
        TokenPayload actual = tokenService.extractPayload("token");
        assertEquals(expected.getUserId(), actual.getUserId());
        assertEquals(expected.getRole(), actual.getRole());
    }
}
