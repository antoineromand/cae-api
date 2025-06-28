package com.pickandeat.authentication.infrastructure.external.encryption;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Tag("unit")
public class PasswordServiceUnitTest {
  private PasswordService passwordService;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @BeforeEach
  public void setUp() {
    passwordService = new PasswordService();
  }

  @Test
  public void shouldHashPassword() {
    String password = "password";
    String hashedPassword = passwordService.hashPassword(password);
    Assertions.assertNotNull(hashedPassword);
    Assertions.assertNotEquals(password, hashedPassword);
    Assertions.assertTrue(hashedPassword.startsWith("$2"));
  }

  @Test
  public void shouldMatchPassword() {
    String password = "password";
    String hashedPassword = passwordService.hashPassword(password);
    boolean result = this.passwordService.matches(password, hashedPassword);
    Assertions.assertTrue(result);
  }

  @Test
  public void shouldNotMatchWrongPassword() {
    String password = "password";
    String hashedPassword = passwordService.hashPassword(password);

    boolean result = passwordService.matches("wrongpassword", hashedPassword);

    Assertions.assertFalse(result);
  }
}
