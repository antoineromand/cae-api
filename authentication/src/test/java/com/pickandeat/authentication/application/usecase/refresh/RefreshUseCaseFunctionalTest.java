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
import org.junit.jupiter.api.BeforeAll;
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
    RefreshUseCase refreshUseCase;

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
        this.registerUseCase.register(command);
        return this.loginUseCase.login(new LoginCommand(command.email(), command.password()));
    }

    @BeforeAll
    void init() {
        this.token = this.createCredentials();
    }

    @Test
    void refreshAccessToken_shouldThrowInvalidTokenException_whenTokenIsMalformed() {
        Assertions.assertThrows(InvalidTokenException.class, () -> this.refreshUseCase.refreshAccessToken("invalid-token"));
    }

    @Test
    void refreshAccessToken_shouldThrowJtiNotFoundInCacheException_whenJtiMissing() {
        String refreshToken = this.tokenService.createRefreshToken(UUID.randomUUID(), "CONSUMER");
        Assertions.assertThrows(JtiNotFoundInCacheException.class, () -> this.refreshUseCase.refreshAccessToken(refreshToken));
    }

    @Test
    void refreshAccessToken_shouldThrowInvalidUserIdInRefreshTokenException_whenUserIdDoesNotMatch() {
        UUID expectedUserId = UUID.randomUUID();
        String refreshToken = this.tokenService.createRefreshToken(expectedUserId, "CONSUMER");
        String expectedJti = this.tokenService.extractJti(refreshToken);
        this.tokenRepository.storeRefreshToken(expectedJti, expectedUserId.toString(), Duration.ofDays(14));

        Assertions.assertThrows(InvalidUserIdInRefreshToken.class, () -> this.refreshUseCase.refreshAccessToken(refreshToken));

    }

    @Test
    void refreshAccessToken_shouldReturnNewRefreshToken_whenTokenIsValid() {
       String refreshToken = this.token.getRefreshToken();

       String generatedRefreshToken = this.refreshUseCase.refreshAccessToken(refreshToken);

       Assertions.assertNotNull(generatedRefreshToken);
       Assertions.assertNotEquals(refreshToken, generatedRefreshToken);

    }



}
