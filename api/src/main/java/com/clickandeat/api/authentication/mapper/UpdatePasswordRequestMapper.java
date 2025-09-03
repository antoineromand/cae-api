package com.clickandeat.api.authentication.mapper;

import com.clickandeat.api.authentication.dto.UpdatePasswordRequestDto;
import com.clickandeat.authentication.application.usecase.update_password.UpdatePasswordCommand;
import java.util.UUID;

public class UpdatePasswordRequestMapper {

  public static UpdatePasswordCommand toCommand(UpdatePasswordRequestDto request, String userId) {
    return new UpdatePasswordCommand(
        UUID.fromString(userId), request.getOldPassword(), request.getNewPassword());
  }
}
