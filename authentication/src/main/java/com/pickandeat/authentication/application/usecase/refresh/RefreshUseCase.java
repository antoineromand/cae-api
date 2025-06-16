package com.pickandeat.authentication.application.usecase.refresh;

import com.pickandeat.authentication.application.exceptions.InvalidTokenException;
import com.pickandeat.authentication.application.exceptions.InvalidUserIdInRefreshToken;
import com.pickandeat.authentication.application.exceptions.JtiNotFoundInCacheException;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.repository.ICredentialsRepository;
import org.springframework.stereotype.Service;

import com.pickandeat.authentication.domain.repository.ITokenRepository;
import com.pickandeat.shared.token.application.TokenService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RefreshUseCase implements IRefreshUseCase {

    private final ITokenRepository tokenRepository;
    private final ICredentialsRepository credentialsRepository;
    private final TokenService tokenService;

    public RefreshUseCase(ITokenRepository tokenRepository, ICredentialsRepository credentialsRepository,
            TokenService tokenService) {
        this.tokenRepository = tokenRepository;
        this.credentialsRepository = credentialsRepository;
        this.tokenService = tokenService;
    }

    @Override
    public String refreshAccessToken(String token) {
        if (!this.tokenService.isRefreshTokenValid(token)) {
            throw new InvalidTokenException("Refresh token is not valid");
        }
        String jti = this.tokenService.extractJti(token);
        String userId = this.tokenRepository.getUserIdByJti(jti);
        if (userId == null) {
            throw new JtiNotFoundInCacheException("Jti not found in cache");
        }
        Credentials credentials = this.credentialsRepository.findByUserId(userId).orElseThrow(() -> new InvalidUserIdInRefreshToken("User id in cache not valid"));
        return this.tokenService.createAccessToken(credentials.getId(), credentials.getRole().name().toString());
    }

}
