package com.clickandeat.shared.token;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
class TokenServiceUnitTest {

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
  void createRefreshToken_shouldDelegateToProvider() {
    Duration expectedDuration = Duration.ofDays(7);
    String expectedToken = "refresh.token";
    when(tokenProvider.generateRefreshToken(any(), any())).thenReturn(expectedToken);
    String token = tokenService.createRefreshToken(userId, role, expectedDuration);
    assertEquals(expectedToken, token);
    verify(tokenProvider).generateRefreshToken(any(), any());
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

  @Test
  void extractExpirationDate_shouldDelegateToProvider() {
    String expectedToken = "access.token";
    Date expectedExpirationDate = Date.from(Instant.now());
    when(tokenProvider.generateAccessToken(any())).thenReturn(expectedToken);
    when(tokenProvider.extractExpirationFromToken(expectedToken))
        .thenReturn(expectedExpirationDate);
    Date result = this.tokenService.extractExpiration(expectedToken);
    assertEquals(expectedExpirationDate, result);
  }

  @Test
  void extractJti_shouldDelegateToProvider() {
    String expectedToken = "access.token";
    String expectedJti = UUID.randomUUID().toString();
    when(tokenProvider.generateAccessToken(any())).thenReturn(expectedToken);
    when(this.tokenProvider.extractJtiFromToken(expectedToken)).thenReturn(expectedJti);
    String result = this.tokenService.extractJti(expectedToken);

    assertEquals(expectedJti, result);
  }

  @Test
  void refreshTokenValid_shouldDelegateToProvider() {
    String invalidToken = "invalid.token";
    String validToken = "valid.token";
    when(tokenProvider.verifyRefreshToken(invalidToken)).thenReturn(false);
    when(tokenProvider.verifyRefreshToken(validToken)).thenReturn(true);
    boolean invalidTokenResult = this.tokenService.isRefreshTokenValid(invalidToken);
    boolean validTokenResult = this.tokenService.isRefreshTokenValid(validToken);
    assertFalse(invalidTokenResult);
    assertTrue(validTokenResult);
    verify(tokenProvider).verifyRefreshToken(validToken);
    verify(tokenProvider).verifyRefreshToken(invalidToken);
  }
}
