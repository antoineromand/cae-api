package com.pickandeat.authentication.infrastructure.repository.cache;

import com.pickandeat.authentication.application.ITokenRepository;
import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

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

  @Override
  public String getUserIdByJti(String jti) {
    return this.redisTemplate.opsForValue().get(jti);
  }

  @Override
  public void deleteByJti(String jti) {
    this.redisTemplate.delete(jti);
  }
}
