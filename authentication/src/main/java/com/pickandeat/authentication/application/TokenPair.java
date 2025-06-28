package com.pickandeat.authentication.application;

public class TokenPair {
  private final String accessToken;
  private final String refreshToken;

  public TokenPair(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public String getAccessToken() {
    return this.accessToken;
  }

  public String getRefreshToken() {
    return this.refreshToken;
  }
}
