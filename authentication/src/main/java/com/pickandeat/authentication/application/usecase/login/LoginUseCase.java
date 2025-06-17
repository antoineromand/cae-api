package com.pickandeat.authentication.application.usecase.login;

import java.time.Duration;
import java.util.UUID;

import com.pickandeat.authentication.domain.repository.ICredentialsRepository;
import org.springframework.stereotype.Service;

import com.pickandeat.authentication.application.exceptions.PasswordNotMatchException;
import com.pickandeat.authentication.application.exceptions.UserNotFoundException;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.repository.ITokenRepository;
import com.pickandeat.authentication.domain.service.IPasswordService;
import com.pickandeat.shared.token.application.TokenService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginUseCase implements ILoginUseCase {
        private final IPasswordService passwordService;
        private final ICredentialsRepository credentialsRepository;
        private final TokenService tokenService;
        private final ITokenRepository tokenRepository;
        private static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);

        public LoginUseCase(IPasswordService passwordService, ICredentialsRepository repository,
                        TokenService tokenService, ITokenRepository tokenRepository) {
                this.passwordService = passwordService;
                this.credentialsRepository = repository;
                this.tokenService = tokenService;
                this.tokenRepository = tokenRepository;
        }

        @Override
        public Token execute(LoginCommand command) {
                Credentials credentials = this.getCredentials(command);
                this.checkPassword(command, credentials.getPassword());
                return this.generateTokens(credentials.getId(), credentials.getRole().name().toString());
        }

        private Credentials getCredentials(LoginCommand command) {
                return this.credentialsRepository.findByEmail(command.email())
                                .orElseThrow(() -> new UserNotFoundException(command.email()));
        }

        private void checkPassword(LoginCommand command, String hashedPassword) {
                if (!this.passwordService.matches(command.password(), hashedPassword)) {
                        throw new PasswordNotMatchException();
                }
        }

        private Token generateTokens(UUID id, String role) {
                String accessToken = this.tokenService.createAccessToken(id, role);
                String refreshToken = this.tokenService.createRefreshToken(id, role, REFRESH_TOKEN_DURATION);
                this.storeRefreshTokenInCache(this.tokenService.extractJti(refreshToken), id.toString(),
                                REFRESH_TOKEN_DURATION);
                return new Token(accessToken, refreshToken);
        }

        private void storeRefreshTokenInCache(String jti, String userId, Duration duration) {
                this.tokenRepository.storeRefreshToken(jti, userId, duration);
        }

}
