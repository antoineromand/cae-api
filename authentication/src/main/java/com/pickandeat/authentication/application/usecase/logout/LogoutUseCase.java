package com.pickandeat.authentication.application.usecase.logout;

import com.pickandeat.authentication.application.exceptions.InvalidTokenException;
import com.pickandeat.authentication.domain.repository.ITokenRepository;
import com.pickandeat.shared.token.application.TokenService;
import org.springframework.stereotype.Service;

@Service
public class LogoutUseCase implements ILogoutUseCase {
    private final ITokenRepository tokenRepository;
    private final TokenService tokenService;
    public LogoutUseCase(ITokenRepository tokenRepository, TokenService tokenService) {
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
    }
    @Override
    public void execute(String refreshToken) {
        this.validateToken(refreshToken);
        this.deleteToken(refreshToken);
    }

    private void validateToken(String refreshToken) {
        if (!this.tokenService.isRefreshTokenValid(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token");
        }
    }

    private void deleteToken(String refreshToken) {
        String jti = this.tokenService.extractJti(refreshToken);
        this.tokenRepository.deleteByJti(jti);
    }
}
