package com.pickandeat.authentication.domain.repository;

import java.time.Duration;

public interface ITokenRepository {
    void storeRefreshToken(String jti, String userId, Duration duration);
    String getUserIdByJti(String jti);
}
