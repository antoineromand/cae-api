package com.clickandeat.authentication.infrastructure.external.encryption;

import com.clickandeat.authentication.domain.service.IPasswordService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService implements IPasswordService {

  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  @Override
  public String hashPassword(String password) {
    return encoder.encode(password);
  }

  @Override
  public boolean matches(String raw, String hashed) {
    return encoder.matches(raw, hashed);
  }
}
