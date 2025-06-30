package com.pickandeat.authentication.application.usecase.login;

import com.pickandeat.authentication.application.ITokenRepository;
import com.pickandeat.authentication.application.TokenPair;
import com.pickandeat.authentication.application.exceptions.application.EmailNotFoundException;
import com.pickandeat.authentication.application.exceptions.application.PasswordNotMatchException;
import com.pickandeat.authentication.application.exceptions.application.RoleMismatchException;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.repository.ICredentialsRepository;
import com.pickandeat.authentication.domain.service.IPasswordService;
import com.pickandeat.shared.token.TokenService;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginUseCase implements ILoginUseCase {
  private final IPasswordService passwordService;
  private final ICredentialsRepository credentialsRepository;
  private final TokenService tokenService;
  private final ITokenRepository tokenRepository;

  public LoginUseCase(
      IPasswordService passwordService,
      ICredentialsRepository repository,
      TokenService tokenService,
      ITokenRepository tokenRepository) {
    this.passwordService = passwordService;
    this.credentialsRepository = repository;
    this.tokenService = tokenService;
    this.tokenRepository = tokenRepository;
  }

  @Override
  public TokenPair execute(LoginCommand command, RoleName expectedRole) {
    Credentials credentials = this.getCredentials(command);
    this.checkPassword(command, credentials.getPassword());
    this.checkRole(expectedRole, credentials.getRole().name());
    return this.generateTokens(credentials.getId(), credentials.getRole().name().toString());
  }

  private Credentials getCredentials(LoginCommand command) {
    return this.credentialsRepository
        .findByEmail(command.email())
        .orElseThrow(EmailNotFoundException::new);
  }

  private void checkRole(RoleName expectedRole, RoleName role) {
    if (!role.equals(expectedRole)) {
      throw new RoleMismatchException();
    }
  }

  private void checkPassword(LoginCommand command, String hashedPassword) {
    if (!this.passwordService.matches(command.password(), hashedPassword)) {
      throw new PasswordNotMatchException();
    }
  }

  private TokenPair generateTokens(UUID id, String role) {
    String accessToken = this.tokenService.createAccessToken(id, role);
    String refreshToken =
        this.tokenService.createRefreshToken(id, role, TokenService.MAX_DURATION_REFRESH_TOKEN);
    this.storeRefreshTokenInCache(this.tokenService.extractJti(refreshToken), id.toString());
    return new TokenPair(accessToken, refreshToken);
  }

  private void storeRefreshTokenInCache(String jti, String userId) {
    this.tokenRepository.storeRefreshToken(jti, userId, TokenService.MAX_DURATION_REFRESH_TOKEN);
  }
}
