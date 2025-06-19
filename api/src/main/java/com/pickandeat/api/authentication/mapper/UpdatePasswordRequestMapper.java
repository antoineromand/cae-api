package com.pickandeat.api.authentication.mapper;

import com.pickandeat.api.authentication.dto.UpdatePasswordRequestDto;
import com.pickandeat.authentication.application.usecase.update_password.UpdatePasswordCommand;

import java.util.UUID;

public class UpdatePasswordRequestMapper {

    public static UpdatePasswordCommand toCommand(UpdatePasswordRequestDto request, String userId) {
        return new UpdatePasswordCommand(UUID.fromString(userId), request.getOldPassword(), request.getNewPassword());
    }
}
