package com.pickandeat.authentication.infrastructure.config.jwt;

import com.pickandeat.shared.token.TokenProvider;
import com.pickandeat.shared.token.TokenService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SecurityJwtConfig.class)
public class SharedJwtTokenConfig {
    @Bean
    public TokenProvider tokenProvider(SecurityJwtConfig config) {
        return new TokenProvider(config.getSecret(), config.getAccessExpirationMs());
    }

    @Bean
    public TokenService tokenService(TokenProvider tokenProvider) {
        return new TokenService(tokenProvider);
    }
}
