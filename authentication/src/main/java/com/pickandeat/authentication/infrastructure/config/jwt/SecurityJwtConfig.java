package com.pickandeat.authentication.infrastructure.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("api.security.jwt")
public class SecurityJwtConfig {
    private String secret;
    private long accessExpirationMs;
    private long refreshExpirationMs;

    public String getSecret() {
        return secret;
    }

    public long getAccessExpirationMs() {
        return accessExpirationMs;
    }

    public long getRefreshExpirationMs() {
        return refreshExpirationMs;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setAccessExpirationMs(long accessExpirationMs) {
        this.accessExpirationMs = accessExpirationMs;
    }

    public void setRefreshExpirationMs(long refreshExpirationMs) {
        this.refreshExpirationMs = refreshExpirationMs;
    }
}
