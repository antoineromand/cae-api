package com.pickandeat.api.authentication.mapper;

import com.pickandeat.api.authentication.dto.LoginRequestDto;
import com.pickandeat.authentication.application.usecase.login.LoginCommand;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
public class LoginRequestMapperUnitTest {
  @Test
  void shouldMapLoginRequestDtoToLoginCommand() {
    LoginRequestDto dto = new LoginRequestDto("test@example.com", "secret");

    LoginCommand command = LoginRequestMapper.toCommand(dto);

    assertEquals("test@example.com", command.email());
    assertEquals("secret", command.password());
  }
}
