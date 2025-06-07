package com.pickandeat.authentication.infrastructure.config.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pickandeat.shared.token.application.TokenService;
import com.pickandeat.shared.token.infrastructure.TokenProvider;

@Configuration
public class SharedJwtTokenConfig {
    @Bean
    public TokenProvider tokenProvider(SecurityJwtConfig config) {
        return new TokenProvider(config.getSecret(), config.getAccessExpirationMs(), config.getRefreshExpirationMs());
    }

    @Bean
    public TokenService tokenService(TokenProvider tokenProvider) {
        return new TokenService(tokenProvider);
    }
}
