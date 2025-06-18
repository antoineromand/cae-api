package com.pickandeat.shared.token.infrastructure;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickandeat.shared.token.domain.ITokenProvider;
import com.pickandeat.shared.token.domain.TokenPayload;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class TokenProvider implements ITokenProvider {

    private String secret;
    private long accessExpirationMs;
    private long refreshExpirationMs;

    public TokenProvider(String secret, long accessExpirationMs, long refreshExpirationMs) {
        this.secret = secret;
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    @Override
    public String generateAccessToken(TokenPayload payload) {
        return buildToken(payload, accessExpirationMs);
    }

    @Override
    public String generateRefreshToken(TokenPayload payload, Duration dynamicExpiration) {
        return buildToken(payload, refreshExpirationMs);
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
        Claims claims = Jwts.parser()
                .verifyWith(this.getSigningKey())
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();

        return new TokenPayload(
                UUID.fromString(claims.getSubject()),
                claims.get("role").toString()
        );
    }

    @Override
    public String extractJtiFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(this.getSigningKey()).build().parseSignedClaims(token).getPayload();

        return claims.getId();
    }

    @Override
    public Date extractExpirationFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(this.getSigningKey()).build().parseSignedClaims(token).getPayload();

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
            Claims claims = Jwts.parser().verifyWith(this.getSigningKey()).build().parseSignedClaims(token)
                    .getPayload();
            return !claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

}
