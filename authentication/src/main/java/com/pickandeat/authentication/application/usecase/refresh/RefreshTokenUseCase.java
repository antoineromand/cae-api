package com.pickandeat.authentication.application.usecase.refresh;

import com.pickandeat.authentication.application.exceptions.InvalidTokenException;
import com.pickandeat.authentication.application.exceptions.application.JtiNotFoundInCacheException;
import com.pickandeat.authentication.application.exceptions.application.UserNotFoundException;
import com.pickandeat.authentication.application.usecase.login.Token;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.repository.ICredentialsRepository;
import com.pickandeat.authentication.domain.repository.ITokenRepository;
import com.pickandeat.shared.token.application.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenUseCase implements IRefreshUseCase {

    private final ITokenRepository tokenRepository;
    private final ICredentialsRepository credentialsRepository;
    private final TokenService tokenService;

    public RefreshTokenUseCase(ITokenRepository tokenRepository, ICredentialsRepository credentialsRepository,
            TokenService tokenService) {
        this.tokenRepository = tokenRepository;
        this.credentialsRepository = credentialsRepository;
        this.tokenService = tokenService;
    }

    @Override
    public Token execute(String token) {
        this.validateToken(token);
        String jti = this.extractJti(token);
        String userId = this.getUserIdFromCache(jti);
        Credentials credentials = this.getCredentials(userId);

        Duration remainingExpiration = this.getRemainingDuration(token);
        this.deleteOldJti(jti);

        Token newTokens = this.createRotatedTokens(credentials, remainingExpiration);
        storeNewJti(newTokens.getRefreshToken(), credentials.getId(), remainingExpiration);

        return newTokens;
    }

    private void validateToken(String token) {
        if (!this.tokenService.isRefreshTokenValid(token)) {
            throw new InvalidTokenException("Refresh token is not valid");
        }
    }

    private String extractJti(String token) {
        return tokenService.extractJti(token);
    }

    private String getUserIdFromCache(String jti) {
        String userId = tokenRepository.getUserIdByJti(jti);
        if (userId == null) throw new JtiNotFoundInCacheException();
        return userId;
    }

    private Credentials getCredentials(String userId) {
        return credentialsRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private void deleteOldJti(String jti) {
        tokenRepository.deleteByJti(jti);
    }

    private void storeNewJti(String refreshToken, UUID userId, Duration ttl) {
        String newJti = tokenService.extractJti(refreshToken);
        tokenRepository.storeRefreshToken(newJti, userId.toString(), ttl);
    }

    private Token createRotatedTokens(Credentials c, Duration ttl) {
        String access  = tokenService.createAccessToken(c.getId(), c.getRole().name().toString());
        String refresh = tokenService.createRefreshToken(c.getId(), c.getRole().name().toString(), ttl);
        return new Token(access, refresh);
    }

    private Duration getRemainingDuration(String oldRefreshToken) {
        Date oldExpiration = this.tokenService.extractExpiration(oldRefreshToken);
        Instant remainingExpiration = oldExpiration.toInstant();
        if (remainingExpiration.isBefore(Instant.now())) {
            throw new InvalidTokenException("Refresh token is already expired");
        }
        return Duration.between(Instant.now(), remainingExpiration);
    }
}
