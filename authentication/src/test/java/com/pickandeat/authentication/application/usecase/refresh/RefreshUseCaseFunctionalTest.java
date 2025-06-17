package com.pickandeat.authentication.application.usecase.refresh;

import com.pickandeat.authentication.TestConfiguration;
import com.pickandeat.authentication.application.exceptions.InvalidTokenException;
import com.pickandeat.authentication.application.exceptions.InvalidUserIdInRefreshToken;
import com.pickandeat.authentication.application.exceptions.JtiNotFoundInCacheException;
import com.pickandeat.authentication.application.usecase.login.LoginCommand;
import com.pickandeat.authentication.application.usecase.login.LoginUseCase;
import com.pickandeat.authentication.application.usecase.login.Token;
import com.pickandeat.authentication.application.usecase.register.RegisterCommand;
import com.pickandeat.authentication.application.usecase.register.RegisterUseCase;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.repository.ITokenRepository;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.authentication.infrastructure.database.AbstractDatabaseContainersTest;
import com.pickandeat.shared.token.application.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@SpringBootTest(classes = TestConfiguration.class)
@Transactional
public class RefreshUseCaseFunctionalTest extends AbstractDatabaseContainersTest {

    @Autowired
    LoginUseCase loginUseCase;

    @Autowired
    RegisterUseCase registerUseCase;

    @Autowired
    RefreshTokenUseCase refreshUseCase;

    @Autowired
    TokenService tokenService;

    @Autowired
    ITokenRepository tokenRepository;

    Token token;

    public Token createCredentials() {
        String dateString = "2025-05-24";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        RegisterCommand command = new RegisterCommand(
                "test-refresh@test.com",
                "MotDePasseTest06?",
                "john",
                "doe",
                "+33650333340",
                LocalDate.parse(dateString, formatter),
                new Role(RoleName.CONSUMER, null));
        this.registerUseCase.execute(command);
        return this.loginUseCase.execute(new LoginCommand(command.email(), command.password()));
    }

    @BeforeEach
    void init() {
        this.token = this.createCredentials();
    }

    @Test
    void refreshAccessToken_shouldThrowInvalidTokenException_whenTokenIsMalformed() {
        Assertions.assertThrows(InvalidTokenException.class, () -> this.refreshUseCase.execute("invalid-token"));
    }

    @Test
    void refreshAccessToken_shouldThrowJtiNotFoundInCacheException_whenJtiMissing() {
        String refreshToken = this.tokenService.createRefreshToken(UUID.randomUUID(), "CONSUMER", Duration.ofDays(1));
        Assertions.assertThrows(JtiNotFoundInCacheException.class, () -> this.refreshUseCase.execute(refreshToken));
    }

    @Test
    void refreshAccessToken_shouldThrowInvalidUserIdInRefreshTokenException_whenUserIdDoesNotMatch() {
        UUID expectedUserId = UUID.randomUUID();
        String refreshToken = this.tokenService.createRefreshToken(expectedUserId, "CONSUMER", Duration.ofDays(3));
        String expectedJti = this.tokenService.extractJti(refreshToken);
        this.tokenRepository.storeRefreshToken(expectedJti, expectedUserId.toString(), Duration.ofDays(3));

        Assertions.assertThrows(InvalidUserIdInRefreshToken.class, () -> this.refreshUseCase.execute(refreshToken));
    }


    @Test
    void refreshAccessToken_shouldReturnNewTokenPair_whenTokenIsValid() {
        String oldRefreshToken = this.token.getRefreshToken();

        Token newTokens = this.refreshUseCase.execute(oldRefreshToken);

        Assertions.assertNotNull(newTokens);
        Assertions.assertNotNull(newTokens.getAccessToken());
        Assertions.assertNotNull(newTokens.getRefreshToken());
        Assertions.assertNotEquals(oldRefreshToken, newTokens.getRefreshToken());
        Assertions.assertNotEquals(this.token.getAccessToken(), newTokens.getAccessToken());
    }

    @Test
    void refreshAccessToken_shouldSupportRotationMultipleTimes() {
        String firstRefresh = token.getRefreshToken();

        Token firstRotation = refreshUseCase.execute(firstRefresh);
        Token secondRotation = refreshUseCase.execute(firstRotation.getRefreshToken());

        Assertions.assertNotEquals(firstRotation.getAccessToken(), secondRotation.getAccessToken());
        Assertions.assertNotEquals(firstRotation.getRefreshToken(), secondRotation.getRefreshToken());
    }
}
