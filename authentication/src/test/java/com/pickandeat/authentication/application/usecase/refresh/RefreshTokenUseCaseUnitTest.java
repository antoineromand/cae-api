package com.pickandeat.authentication.application.usecase.refresh;

import com.pickandeat.authentication.application.exceptions.InvalidTokenException;
import com.pickandeat.authentication.application.exceptions.InvalidUserIdInRefreshToken;
import com.pickandeat.authentication.application.exceptions.JtiNotFoundInCacheException;
import com.pickandeat.authentication.application.usecase.login.Token;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.repository.ICredentialsRepository;
import com.pickandeat.authentication.domain.repository.ITokenRepository;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.shared.token.application.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RefreshTokenUseCaseUnitTest {

    private ICredentialsRepository credentialsRepository;
    private ITokenRepository tokenRepository;
    private TokenService tokenService;
    private RefreshTokenUseCase refreshTokenUseCase;

    @BeforeEach
    void setUp() {
        credentialsRepository = mock(ICredentialsRepository.class);
        tokenRepository = mock(ITokenRepository.class);
        tokenService = mock(TokenService.class);
        refreshTokenUseCase = new RefreshTokenUseCase(tokenRepository, credentialsRepository, tokenService);
    }

    @Test
    void refreshAccessToken_shouldThrowInvalidTokenException_whenTokenIsInvalid() {
        String invalidToken = "invalid-token";
        when(tokenService.isRefreshTokenValid(invalidToken)).thenReturn(false);

        assertThrows(InvalidTokenException.class, () -> refreshTokenUseCase.execute(invalidToken));

        verify(tokenRepository, never()).getUserIdByJti(any());
        verify(credentialsRepository, never()).findByUserId(any());
    }

    @Test
    void refreshAccessToken_shouldThrowJtiNotFoundInCacheException_whenJtiIsMissing() {
        String validToken = "valid-token";
        String jti = "some-jti";

        when(tokenService.isRefreshTokenValid(validToken)).thenReturn(true);
        when(tokenService.extractJti(validToken)).thenReturn(jti);
        when(tokenRepository.getUserIdByJti(jti)).thenReturn(null);

        assertThrows(JtiNotFoundInCacheException.class, () -> refreshTokenUseCase.execute(validToken));
    }

    @Test
    void refreshAccessToken_shouldThrowInvalidUserIdInRefreshTokenException_whenUserIsNotFound() {
        String validToken = "valid-token";
        String jti = "jti-value";
        String userId = UUID.randomUUID().toString();

        when(tokenService.isRefreshTokenValid(validToken)).thenReturn(true);
        when(tokenService.extractJti(validToken)).thenReturn(jti);
        when(tokenRepository.getUserIdByJti(jti)).thenReturn(userId);
        when(credentialsRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(InvalidUserIdInRefreshToken.class, () -> refreshTokenUseCase.execute(validToken));
    }

    @Test
    void refreshAccessToken_shouldThrowInvalidTokenException_whenTokenIsExpired() {
        String expiredToken = "expired-token";
        String jti = "expired-jti";
        String userId = UUID.randomUUID().toString();
        Credentials credentials = mock(Credentials.class);

        when(tokenService.isRefreshTokenValid(expiredToken)).thenReturn(true);
        when(tokenService.extractJti(expiredToken)).thenReturn(jti);
        when(tokenRepository.getUserIdByJti(jti)).thenReturn(userId);
        when(credentialsRepository.findByUserId(userId)).thenReturn(Optional.of(credentials));
        when(tokenService.extractExpiration(expiredToken)).thenReturn(Date.from(Instant.now().minusSeconds(10)));

        assertThrows(InvalidTokenException.class, () -> refreshTokenUseCase.execute(expiredToken));
    }

    private Credentials createFakeCredentials() {
        return new Credentials(UUID.randomUUID(), "test@test.com", "A.Hefjizie99", new Role(RoleName.CONSUMER, null), Date.from(Instant.now()),
                null);
    }

    @Test
    void refreshAccessToken_shouldReturnNewTokens_whenTokenIsValid() {
        String token            = "valid-refresh-token";
        String oldJti           = "jti-old";
        String newAccessToken   = "new-access-token";
        String newRefreshToken  = "new-refresh-token";
        String newJti           = "jti-new";
        Date   expiryDate       = Date.from(Instant.now().plusSeconds(300));

        Credentials creds = createFakeCredentials();

        when(tokenService.isRefreshTokenValid(token)).thenReturn(true);
        when(tokenService.extractJti(token)).thenReturn(oldJti);
        when(tokenRepository.getUserIdByJti(oldJti)).thenReturn(creds.getId().toString());
        when(credentialsRepository.findByUserId(creds.getId().toString())).thenReturn(Optional.of(creds));
        when(tokenService.extractExpiration(token)).thenReturn(expiryDate);

        when(tokenService.createAccessToken(eq(creds.getId()), eq(creds.getRole().name().toString())))
                .thenReturn(newAccessToken);

        when(tokenService.createRefreshToken(eq(creds.getId()), eq(creds.getRole().name().toString()), any(Duration.class)))
                .thenReturn(newRefreshToken);

        when(tokenService.extractJti(newRefreshToken)).thenReturn(newJti);

        Token result = refreshTokenUseCase.execute(token);

        assertEquals(newAccessToken,  result.getAccessToken());
        assertEquals(newRefreshToken, result.getRefreshToken());

        verify(tokenRepository).deleteByJti(oldJti);
        verify(tokenRepository).storeRefreshToken(eq(newJti),
                eq(creds.getId().toString()),
                any(Duration.class));
    }

}
