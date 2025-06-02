package com.pickandeat.api.authentication.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import com.pickandeat.api.authentication.dto.validators.ValidRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public class RegisterRequestDto {
    @Email(message = "email form not valid.")
    @NotBlank(message = "email must be provided.")
    private final String email;

    @NotBlank(message = "password must be provided.")
    @Length(min = 8, message = "password must contains at least 8 characters.")
    private final String password;

    @NotBlank(message = "firstName must be provided.")
    private final String firstName;

    @NotBlank(message = "lastName must be provided.")
    private final String lastName;

    @NotBlank(message = "phoneNumber must be provided.")
    private final String phoneNumber;

    @NotNull(message = "birthDate must be provided.")
    @Past(message = "birthDate must be in the past")
    private final LocalDate birthDate;

    @NotBlank(message = "role must be provided.")
    @ValidRole
    private final String role;

    public RegisterRequestDto(String email, String password, String firstName, String lastName, String phoneNumber,
            LocalDate birthDate, String role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getRole() {
        return role;
    }

}
