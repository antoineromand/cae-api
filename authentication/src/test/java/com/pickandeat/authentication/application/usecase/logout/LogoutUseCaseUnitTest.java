package com.pickandeat.authentication.application.usecase.logout;

import com.pickandeat.authentication.application.exceptions.InvalidTokenException;
import com.pickandeat.authentication.domain.repository.ITokenRepository;
import com.pickandeat.shared.token.application.TokenService;
import com.pickandeat.shared.token.domain.ITokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class LogoutUseCaseUnitTest {
    private LogoutUseCase logoutUseCase;
    private TokenService tokenService;
    private ITokenRepository tokenRepository;

    @BeforeEach
    void init() {

        this.tokenRepository = mock(ITokenRepository.class);
        this.tokenService = mock(TokenService.class);
        this.logoutUseCase = new LogoutUseCase(tokenRepository, tokenService);
    }

    @Test
    void logout_shouldThrowException_whenTokenIsNull() {
        Assertions.assertThrows(InvalidTokenException.class, () -> logoutUseCase.execute(null));
    }

    @Test
    void logout_shouldThrowException_whenTokenNotValid() {
        String refreshToken = "refresh-token";
        String expectedJti = "jti";
        when(this.tokenService.isRefreshTokenValid(refreshToken)).thenReturn(Boolean.FALSE);

        Assertions.assertThrows(InvalidTokenException.class, () -> logoutUseCase.execute(refreshToken));

        verify(this.tokenService, times(0)).extractJti(refreshToken);
        verify(this.tokenRepository, times(0)).deleteByJti(expectedJti);

    }


    @Test
    void logout_shouldLogout_whenTokenValid() {
        String refreshToken = "refresh-token";
        String expectedJti = "jti";

        when(this.tokenService.isRefreshTokenValid(refreshToken)).thenReturn(Boolean.TRUE);
        when(this.tokenService.extractJti(refreshToken)).thenReturn(expectedJti);

        this.logoutUseCase.execute(refreshToken);

        verify(this.tokenRepository, times(1)).deleteByJti(expectedJti);
    }
}
