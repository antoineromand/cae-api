package com.pickandeat.authentication.application.usecase.login;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.pickandeat.authentication.application.ITokenRepository;
import com.pickandeat.authentication.application.TokenPair;
import com.pickandeat.authentication.application.exceptions.application.EmailNotFoundException;
import com.pickandeat.authentication.application.exceptions.application.PasswordNotMatchException;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.repository.ICredentialsRepository;
import com.pickandeat.authentication.domain.service.IPasswordService;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.shared.token.ITokenProvider;
import com.pickandeat.shared.token.TokenPayload;
import com.pickandeat.shared.token.TokenService;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
public class LoginUseCaseUnitTest {

  private ICredentialsRepository credentialsRepository;
  private IPasswordService passwordService;
  private ITokenProvider tokenProvider;
  private TokenService tokenService;
  private ITokenRepository tokenRepository;
  private ILoginUseCase loginUseCase;

  @BeforeEach
  void init() {
    credentialsRepository = mock(ICredentialsRepository.class);
    passwordService = mock(IPasswordService.class);
    tokenProvider = mock(ITokenProvider.class);
    tokenRepository = mock(ITokenRepository.class);
    tokenService = new TokenService(tokenProvider);
    loginUseCase =
        new LoginUseCase(passwordService, credentialsRepository, tokenService, tokenRepository);
  }

  private LoginCommand generateCommand() {
    return new LoginCommand("example@example.fr", "StrongPassword06?");
  }

  @Test
  void login_shouldThrowUserNotFoundException_whenEmailDoesNotExist() {
    LoginCommand command = generateCommand();

    when(credentialsRepository.findByEmail(command.email())).thenReturn(Optional.empty());

    assertThrows(EmailNotFoundException.class, () -> loginUseCase.execute(command, RoleName.CONSUMER));
  }

  // Add test to trigger exception if user has a unexpected role.

  @Test
  void login_shouldThrowPasswordNotMatchException_whenPasswordIsIncorrect() {
    LoginCommand command = generateCommand();
    Credentials credentials = mock(Credentials.class);
    Role consumerRole = new Role(RoleName.CONSUMER, null);
    when(credentials.getRole()).thenReturn(consumerRole);
    when(credentialsRepository.findByEmail(command.email())).thenReturn(Optional.of(credentials));
    when(credentials.getPassword()).thenReturn("hashed-password");
    when(passwordService.matches(command.password(), "hashed-password")).thenReturn(false);

    assertThrows(PasswordNotMatchException.class, () -> loginUseCase.execute(command, RoleName.CONSUMER));
  }

  @Test
  void login_shouldReturnTokens_whenCredentialsAreValid() {
    LoginCommand command = generateCommand();
    UUID userId = UUID.randomUUID();
    Role role = new Role(RoleName.CONSUMER, null);
    Credentials credentials = mock(Credentials.class);

    when(credentials.getId()).thenReturn(userId);
    when(credentials.getRole()).thenReturn(role);
    when(credentials.getPassword()).thenReturn("hashed-password");

    when(credentialsRepository.findByEmail(command.email())).thenReturn(Optional.of(credentials));
    when(passwordService.matches(command.password(), "hashed-password")).thenReturn(true);

    when(tokenProvider.generateAccessToken(any(TokenPayload.class))).thenReturn("access-token");
    when(tokenProvider.generateRefreshToken(any(TokenPayload.class), any(Duration.class)))
        .thenReturn("refresh-token");
    when(tokenProvider.extractJtiFromToken("refresh-token")).thenReturn("jti-value");

    TokenPair result = loginUseCase.execute(command, RoleName.CONSUMER);

    assertNotNull(result);
    assertEquals("access-token", result.getAccessToken());
    assertEquals("refresh-token", result.getRefreshToken());
  }

  @Test
  void login_shouldStoreRefreshTokenWithCorrectParameters_whenLoginIsSuccessful() {
    LoginCommand command = generateCommand();
    UUID userId = UUID.randomUUID();
    Role role = new Role(RoleName.CONSUMER, null);
    Credentials credentials = mock(Credentials.class);

    when(credentials.getId()).thenReturn(userId);
    when(credentials.getRole()).thenReturn(role);
    when(credentials.getPassword()).thenReturn("hashed-password");

    when(credentialsRepository.findByEmail(command.email())).thenReturn(Optional.of(credentials));
    when(passwordService.matches(command.password(), "hashed-password")).thenReturn(true);

    when(tokenProvider.generateAccessToken(any(TokenPayload.class))).thenReturn("access-token");
    when(tokenProvider.generateRefreshToken(any(TokenPayload.class), any(Duration.class)))
        .thenReturn("refresh-token");
    when(tokenProvider.extractJtiFromToken("refresh-token")).thenReturn("jti-value");

    loginUseCase.execute(command, RoleName.CONSUMER);

    verify(tokenRepository)
        .storeRefreshToken(
            eq("jti-value"), eq(userId.toString()), eq(TokenService.MAX_DURATION_REFRESH_TOKEN));
  }
}
