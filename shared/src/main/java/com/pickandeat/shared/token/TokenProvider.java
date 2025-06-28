package com.pickandeat.shared.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;

public class TokenProvider implements ITokenProvider {

  private final String secret;
  private final long accessExpirationMs;

  public TokenProvider(String secret, long accessExpirationMs) {
    this.secret = secret;
    this.accessExpirationMs = accessExpirationMs;
  }

  @Override
  public String generateAccessToken(TokenPayload payload) {
    return buildToken(payload, accessExpirationMs);
  }

  @Override
  public String generateRefreshToken(TokenPayload payload, Duration dynamicExpiration) {
    return buildToken(payload, dynamicExpiration.toMillis());
  }

  @Override
  public boolean verifyAccessToken(String accessToken) {
    return validate(accessToken);
  }

  @Override
  public boolean verifyRefreshToken(String refreshToken) {
    return validate(refreshToken);
  }

  @Override
  public TokenPayload decodeAccessToken(String accessToken) {
    Claims claims =
        Jwts.parser()
            .verifyWith(this.getSigningKey())
            .build()
            .parseSignedClaims(accessToken)
            .getPayload();

    return new TokenPayload(UUID.fromString(claims.getSubject()), claims.get("role").toString());
  }

  @Override
  public String extractJtiFromToken(String token) {
    Claims claims =
        Jwts.parser()
            .verifyWith(this.getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();

    return claims.getId();
  }

  @Override
  public Date extractExpirationFromToken(String token) {
    Claims claims =
        Jwts.parser()
            .verifyWith(this.getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();

    return claims.getExpiration();
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(this.secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private String buildToken(TokenPayload payload, Long expirationMs) {
    Instant now = Instant.now();
    String jti = UUID.randomUUID().toString();
    return Jwts.builder()
        .id(jti)
        .subject(payload.getUserId().toString())
        .claim("role", payload.getRole())
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(expirationMs)))
        .signWith(this.getSigningKey())
        .compact();
  }

  private boolean validate(String token) {
    if (token == null || token.isEmpty()) {
      return false;
    }
    try {
      Claims claims =
          Jwts.parser()
              .verifyWith(this.getSigningKey())
              .build()
              .parseSignedClaims(token)
              .getPayload();
      return !claims.getExpiration().before(new Date());
    } catch (JwtException e) {
      return false;
    }
  }
}
