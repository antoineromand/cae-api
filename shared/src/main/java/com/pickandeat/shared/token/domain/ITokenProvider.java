package com.pickandeat.shared.token.domain;

public interface ITokenProvider {
    String generateAccessToken(TokenPayload payload);

    String generateRefreshToken(TokenPayload payload);

    boolean verifyAccessToken(String accessToken);

    boolean verifyRefreshToken(String refreshToken);

    TokenPayload decodeAccessToken(String accessToken);
}
