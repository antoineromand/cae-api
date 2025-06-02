package com.pickandeat.api.authentication.mapper;

import com.pickandeat.api.authentication.dto.RegisterRequestDto;
import com.pickandeat.authentication.application.usecase.RegisterCommand;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.valueobject.Role;

public class RegisterRequestMapper {
    public static RegisterCommand toCommand(RegisterRequestDto dto) {
        RoleName roleName = RoleName.valueOf(dto.getRole().toUpperCase());
        Role role = new Role(roleName, null);
        return new RegisterCommand(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName(),
                dto.getPhoneNumber(), dto.getBirthDate(), role);
    }
}
