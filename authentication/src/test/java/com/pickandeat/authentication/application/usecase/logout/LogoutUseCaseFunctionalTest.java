package com.pickandeat.authentication.application.usecase.logout;

import com.pickandeat.authentication.application.ITokenRepository;
import com.pickandeat.authentication.application.exceptions.application.InvalidTokenException;
import com.pickandeat.authentication.infrastructure.database.AbstractDatabaseContainersTest;
import com.pickandeat.shared.token.TokenService;

import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("functional")
public class LogoutUseCaseFunctionalTest extends AbstractDatabaseContainersTest {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ITokenRepository tokenRepository;

    @Autowired
    private LogoutUseCase logoutUseCase;

    @Test
    void logout_shouldThrowException_whenTokenIsNull() {
        Assertions.assertThrows(InvalidTokenException.class, () -> this.logoutUseCase.execute(null));
    }

    @Test
    void logout_shouldThrowException_whenTokenIsInvalid() {
        Assertions.assertThrows(
                InvalidTokenException.class, () -> this.logoutUseCase.execute("invalid-refresh-token"));
    }

    @Test
    void logout_shouldLogout_whenTokenIsValid() {
        UUID userId = UUID.randomUUID();
        String refreshToken =
                this.tokenService.createRefreshToken(userId, "CONSUMER", Duration.ofDays(3));

        String jti = this.tokenService.extractJti(refreshToken);

        this.tokenRepository.storeRefreshToken(jti, userId.toString(), Duration.ofDays(3));

        this.logoutUseCase.execute(refreshToken);

        Assertions.assertNull(this.tokenRepository.getUserIdByJti(jti));
    }
}
