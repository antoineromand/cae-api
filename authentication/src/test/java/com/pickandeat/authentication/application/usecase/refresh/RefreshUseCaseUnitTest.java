package com.pickandeat.authentication.application.usecase.refresh;

import com.pickandeat.authentication.application.exceptions.InvalidTokenException;
import com.pickandeat.authentication.application.exceptions.InvalidUserIdInRefreshToken;
import com.pickandeat.authentication.application.exceptions.JtiNotFoundInCacheException;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.repository.ICredentialsRepository;
import com.pickandeat.authentication.domain.repository.ITokenRepository;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.shared.token.application.TokenService;
import com.pickandeat.shared.token.domain.ITokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RefreshUseCaseUnitTest {

    private ICredentialsRepository credentialsRepository;
    private ITokenRepository tokenRepository;
    private ITokenProvider tokenProvider;

    @BeforeEach
    void init() {
        credentialsRepository = mock(ICredentialsRepository.class);
        tokenRepository = mock(ITokenRepository.class);
        tokenProvider = mock(ITokenProvider.class);
    }

    @Test
    void refreshAccessToken_shouldThrowInvalidTokenException_whenTokenIsInvalid() {
        String invalidToken = "not-valid-token";
        TokenService tokenServiceMock = mock(TokenService.class);
        when(tokenServiceMock.isRefreshTokenValid(invalidToken)).thenReturn(false);

        RefreshUseCase refresh = new RefreshUseCase(tokenRepository, credentialsRepository, tokenServiceMock);

        assertThrows(InvalidTokenException.class, () -> refresh.refreshAccessToken(invalidToken));

        verify(tokenRepository, never()).getUserIdByJti(any());
        verify(credentialsRepository, never()).findByUserId(any());
    }

    @Test
    void refreshAccessToken_shouldThrowJtiNotFoundInCacheException_whenJtiIsMissing() {
        String validToken = "valid-token";
        String jti = "jti-value";

        TokenService tokenServiceMock = mock(TokenService.class);
        when(tokenServiceMock.isRefreshTokenValid(validToken)).thenReturn(true);
        when(tokenServiceMock.extractJti(validToken)).thenReturn(jti);
        when(tokenRepository.getUserIdByJti(jti)).thenReturn(null);

        RefreshUseCase refresh = new RefreshUseCase(tokenRepository, credentialsRepository, tokenServiceMock);

        assertThrows(JtiNotFoundInCacheException.class, () -> refresh.refreshAccessToken(validToken));
    }

    @Test
    void refreshAccessToken_shouldThrowInvalidUserIdInRefreshTokenException_whenUserIsNotFound() {
        String validToken = "valid-token";
        String jti = "jti-value";
        String userId = UUID.randomUUID().toString();

        TokenService tokenServiceMock = mock(TokenService.class);
        when(tokenServiceMock.isRefreshTokenValid(validToken)).thenReturn(true);
        when(tokenServiceMock.extractJti(validToken)).thenReturn(jti);
        when(tokenRepository.getUserIdByJti(jti)).thenReturn(userId);
        when(credentialsRepository.findByUserId(userId)).thenReturn(Optional.empty());

        RefreshUseCase refresh = new RefreshUseCase(tokenRepository, credentialsRepository, tokenServiceMock);

        assertThrows(InvalidUserIdInRefreshToken.class, () -> refresh.refreshAccessToken(validToken));
    }

    @Test
    void refreshAccessToken_shouldReturnNewAccessToken_whenRefreshTokenIsValid() {
        String validToken = "valid-token";
        String jti = "jti-value";
        UUID userId = UUID.randomUUID();
        String expectedAccessToken = "new-access-token";

        Credentials mockCredentials = mock(Credentials.class);
        when(mockCredentials.getId()).thenReturn(userId);
        when(mockCredentials.getRole()).thenReturn(new Role(RoleName.CONSUMER, null));

        TokenService tokenServiceMock = mock(TokenService.class);
        when(tokenServiceMock.isRefreshTokenValid(validToken)).thenReturn(true);
        when(tokenServiceMock.extractJti(validToken)).thenReturn(jti);
        when(tokenServiceMock.createAccessToken(userId, "CONSUMER")).thenReturn(expectedAccessToken);

        when(tokenRepository.getUserIdByJti(jti)).thenReturn(userId.toString());
        when(credentialsRepository.findByUserId(userId.toString())).thenReturn(Optional.of(mockCredentials));

        RefreshUseCase refresh = new RefreshUseCase(tokenRepository, credentialsRepository, tokenServiceMock);

        String result = refresh.refreshAccessToken(validToken);

        assertEquals(expectedAccessToken, result);
        verify(tokenServiceMock).createAccessToken(userId, "CONSUMER");
    }
}
