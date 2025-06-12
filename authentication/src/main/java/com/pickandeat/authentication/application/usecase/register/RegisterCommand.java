package com.pickandeat.authentication.application.usecase.register;

import java.time.LocalDate;

import com.pickandeat.authentication.domain.valueobject.Role;

public record RegisterCommand(String email, String password, String firstName, String lastName, String phoneNumber, LocalDate birthDate, Role role) {}
