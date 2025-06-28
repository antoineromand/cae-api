package com.pickandeat.api.authentication.mapper;

import com.pickandeat.api.authentication.dto.LoginRequestDto;
import com.pickandeat.authentication.application.usecase.login.LoginCommand;

public class LoginRequestMapper {
    public static LoginCommand toCommand(LoginRequestDto loginRequestDto) {
        return new LoginCommand(loginRequestDto.getEmail(), loginRequestDto.getPassword());
    }
}
