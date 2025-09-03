package com.clickandeat.api.authentication.mapper;

import com.clickandeat.api.authentication.dto.LoginRequestDto;
import com.clickandeat.authentication.application.usecase.login.LoginCommand;

public class LoginRequestMapper {
  public static LoginCommand toCommand(LoginRequestDto loginRequestDto) {
    return new LoginCommand(loginRequestDto.getEmail(), loginRequestDto.getPassword());
  }
}
