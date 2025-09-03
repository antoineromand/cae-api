package com.clickandeat.api.authentication.mapper;

import com.clickandeat.api.authentication.dto.RegisterRequestDto;
import com.clickandeat.authentication.application.usecase.register.RegisterCommand;
import com.clickandeat.authentication.domain.valueobject.Role;
import com.clickandeat.shared.enums.RoleName;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RegisterRequestMapper {
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

  public static RegisterCommand toCommand(RegisterRequestDto dto) {
    RoleName roleName = RoleName.valueOf(dto.getRole().toUpperCase());
    Role role = new Role(roleName, null);

    return new RegisterCommand(
        dto.getEmail(),
        dto.getPassword(),
        dto.getFirstName(),
        dto.getLastName(),
        dto.getPhoneNumber(),
        LocalDate.parse(dto.getBirthDate()),
        role);
  }
}
