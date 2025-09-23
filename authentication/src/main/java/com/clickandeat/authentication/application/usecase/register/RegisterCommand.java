package com.clickandeat.authentication.application.usecase.register;

import com.clickandeat.authentication.domain.valueobject.Role;
import java.time.LocalDate;

public record RegisterCommand(
    String email,
    String password,
    String firstName,
    String lastName,
    String phoneNumber,
    LocalDate birthDate,
    Role role) {}
