package com.clickandeat.authentication.infrastructure.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties("api.security.jwt")
@EnableConfigurationProperties
public class SecurityJwtConfig {
  private String secret;
  private long accessExpirationMs;

  public String getSecret() {
    return secret;
  }

  public long getAccessExpirationMs() {
    return accessExpirationMs;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public void setAccessExpirationMs(long accessExpirationMs) {
    this.accessExpirationMs = accessExpirationMs;
  }
}
