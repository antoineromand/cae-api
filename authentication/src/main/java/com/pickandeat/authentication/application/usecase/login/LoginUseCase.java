package com.pickandeat.authentication.application.usecase.login;


import com.pickandeat.authentication.application.exceptions.application.PasswordNotMatchException;
import com.pickandeat.authentication.application.exceptions.application.UserNotFoundException;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.repository.ICredentialsRepository;
import com.pickandeat.authentication.domain.repository.ITokenRepository;
import com.pickandeat.authentication.domain.service.IPasswordService;
import com.pickandeat.shared.token.application.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class LoginUseCase implements ILoginUseCase {
        private final IPasswordService passwordService;
        private final ICredentialsRepository credentialsRepository;
        private final TokenService tokenService;
        private final ITokenRepository tokenRepository;

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
                        .orElseThrow(UserNotFoundException::new);
        }

        private void checkPassword(LoginCommand command, String hashedPassword) {
                if (!this.passwordService.matches(command.password(), hashedPassword)) {
                        throw new PasswordNotMatchException();
                }
        }

        private Token generateTokens(UUID id, String role) {
                String accessToken = this.tokenService.createAccessToken(id, role);
                String refreshToken = this.tokenService.createRefreshToken(id, role, TokenService.MAX_DURATION_REFRESH_TOKEN);
                this.storeRefreshTokenInCache(this.tokenService.extractJti(refreshToken), id.toString());
                return new Token(accessToken, refreshToken);
        }

        private void storeRefreshTokenInCache(String jti, String userId) {
                this.tokenRepository.storeRefreshToken(jti, userId, TokenService.MAX_DURATION_REFRESH_TOKEN);
        }

}
