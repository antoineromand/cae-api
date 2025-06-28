package com.pickandeat.api.authentication.mapper;

import com.pickandeat.api.authentication.dto.UpdatePasswordRequestDto;
import com.pickandeat.authentication.application.usecase.update_password.UpdatePasswordCommand;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
public class UpdatePasswordRequestMapperUnitTest {
  @Test
  void shouldMapRequestDtoToCommand() {
    UpdatePasswordRequestDto dto = new UpdatePasswordRequestDto("old", "new");
    UUID userId = UUID.randomUUID();

    UpdatePasswordCommand command = UpdatePasswordRequestMapper.toCommand(dto, userId.toString());

    assertEquals(userId, command.userId());
    assertEquals("old", command.oldPassword());
    assertEquals("new", command.newPassword());
  }

  @Test
  void shouldThrowExceptionWhenUserIdInvalid() {
    UpdatePasswordRequestDto dto = new UpdatePasswordRequestDto("old", "new");

    String invalidUuid = "not-a-uuid";

    assertThrows(
            IllegalArgumentException.class,
            () -> {
              UpdatePasswordRequestMapper.toCommand(dto, invalidUuid);
            });
  }
}
