package com.pickandeat.authentication.infrastructure.repository.cache;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.pickandeat.authentication.domain.repository.ITokenRepository;

@Repository
public class TokenRepositoryImpl implements ITokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void storeRefreshToken(String jti, String userId, Duration duration) {
        this.redisTemplate.opsForValue().set(jti, userId, duration);
    }

    public String getUserIdByJti(String jti) {
        return this.redisTemplate.opsForValue().get(jti);
    }

}
