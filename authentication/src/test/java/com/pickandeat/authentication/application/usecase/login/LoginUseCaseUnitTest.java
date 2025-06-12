package com.pickandeat.authentication.application.usecase.login;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pickandeat.authentication.application.exceptions.PasswordNotMatchException;
import com.pickandeat.authentication.application.exceptions.UserNotFoundException;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.repository.ICredentialsRespository;
import com.pickandeat.authentication.domain.repository.ITokenRepository;
import com.pickandeat.authentication.domain.service.IPasswordService;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.shared.token.application.TokenService;
import com.pickandeat.shared.token.domain.ITokenProvider;

public class LoginUseCaseUnitTest {
    private ICredentialsRespository credentialsRespository;
    private IPasswordService passwordService;
    private ILoginUseCase loginUseCase;
    private ITokenProvider tokenProvider;
    private TokenService TokenService;
    private ITokenRepository tokenRepository;

    @BeforeEach
    void init() {
        this.credentialsRespository = mock(ICredentialsRespository.class);
        this.passwordService = mock(IPasswordService.class);
        this.tokenProvider = mock(ITokenProvider.class);
        this.tokenRepository = mock(ITokenRepository.class);
        this.TokenService = new TokenService(tokenProvider);
        this.loginUseCase = new LoginUseCase(passwordService, credentialsRespository, TokenService, tokenRepository);
    }

    private LoginCommand generateCommand() {
        return new LoginCommand("example@example.fr", "StrongPassword06?");
    }

    @Test
    public void shouldThrowExceptionIfUserNotFound() {
        LoginCommand command = generateCommand();

        when(this.credentialsRespository.findByEmail(command.email())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            this.loginUseCase.login(command);
        });
    }

    @Test
    void shouldThrowExceptionIfPasswordNotMatch() {
        LoginCommand command = generateCommand();

        Credentials existingCredentials = mock(Credentials.class);

        when(this.credentialsRespository.findByEmail(command.email())).thenReturn(Optional.of(existingCredentials));

        when(this.passwordService.matches(command.password(), existingCredentials.getPassword())).thenReturn(false);

        assertThrows(PasswordNotMatchException.class, () -> {
            this.loginUseCase.login(command);
        });
    }

    @Test
    void shouldReturnTokensIfLoginIsSuccessful() {
        LoginCommand command = generateCommand();
        UUID userId = UUID.randomUUID();
        Role consumerRole = new Role(RoleName.CONSUMER, null);

        Credentials credentials = mock(Credentials.class);
        when(credentials.getId()).thenReturn(userId);
        when(credentials.getRole()).thenReturn(consumerRole);
        when(credentials.getPassword()).thenReturn("hashed");

        when(this.credentialsRespository.findByEmail(command.email())).thenReturn(Optional.of(credentials));
        when(this.passwordService.matches(command.password(), "hashed")).thenReturn(true);
        when(this.tokenProvider.generateAccessToken(any())).thenReturn("access-token");
        when(this.tokenProvider.generateRefreshToken(any())).thenReturn("refresh-token");
        when(this.tokenProvider.extractJtiFromToken("refresh-token")).thenReturn("jti-value");

        Token resultToken = this.loginUseCase.login(command);

        assertEquals("access-token", resultToken.getAccessToken());
        assertEquals("refresh-token", resultToken.getRefreshToken());
    }

    @Test
    void shouldStoreRefreshTokenWithCorrectParameters() {
        LoginCommand command = generateCommand();
        UUID userId = UUID.randomUUID();
        Role consumerRole = new Role(RoleName.CONSUMER, null);

        Credentials credentials = mock(Credentials.class);
        when(credentials.getId()).thenReturn(userId);
        when(credentials.getRole()).thenReturn(consumerRole);
        when(credentials.getPassword()).thenReturn("hashed");

        when(this.credentialsRespository.findByEmail(command.email())).thenReturn(Optional.of(credentials));
        when(this.passwordService.matches(command.password(), "hashed")).thenReturn(true);
        when(this.tokenProvider.generateAccessToken(any())).thenReturn("access-token");
        when(this.tokenProvider.generateRefreshToken(any())).thenReturn("refresh-token");
        when(this.tokenProvider.extractJtiFromToken("refresh-token")).thenReturn("jti-value");

        this.loginUseCase.login(command);

        verify(tokenRepository).storeRefreshToken(
                eq("jti-value"),
                eq(userId.toString()),
                eq(Duration.ofMillis(1209600000)));
    }

}
