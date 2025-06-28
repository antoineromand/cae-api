package com.pickandeat.authentication.application.usecase.register;

import com.pickandeat.authentication.domain.valueobject.Role;

import java.time.LocalDate;

public record RegisterCommand(
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate birthDate,
        Role role) {
}
