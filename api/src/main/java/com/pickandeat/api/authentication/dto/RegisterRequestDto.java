package com.pickandeat.api.authentication.dto;

import org.hibernate.validator.constraints.Length;

import com.pickandeat.api.authentication.dto.validators.ValidDate;
import com.pickandeat.api.authentication.dto.validators.ValidRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

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

    @ValidDate
    @NotBlank(message = "birthDate must be provided.")
    private final String birthDate;

    @NotBlank(message = "role must be provided.")
    @ValidRole
    private final String role;

    public RegisterRequestDto(String email, String password, String firstName, String lastName, String phoneNumber,
            String birthDate, String role) {
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

    public String getBirthDate() {
        return birthDate;
    }

    public String getRole() {
        return role;
    }

}
