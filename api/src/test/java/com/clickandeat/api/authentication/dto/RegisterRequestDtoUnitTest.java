package com.clickandeat.api.authentication.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
public class RegisterRequestDtoUnitTest {

  private static Validator validator;

  @BeforeAll
  static void setupValidator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  private RegisterRequestDto validDto() {
    return new RegisterRequestDto(
        "john.doe@example.com",
        "StrongPass1!",
        "John",
        "Doe",
        "0601020304",
        "2000-01-01",
        "CONSUMER");
  }

  @Test
  void shouldPassValidation_whenAllFieldsAreValid() {
    Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(validDto());
    assertThat(violations).isEmpty();
  }

  @Test
  void shouldFailValidation_whenEmailIsBlank() {
    var dto =
        new RegisterRequestDto(
            "",
            validDto().getPassword(),
            validDto().getFirstName(),
            validDto().getLastName(),
            validDto().getPhoneNumber(),
            validDto().getBirthDate(),
            validDto().getRole());

    assertViolation(dto, "email");
  }

  @Test
  void shouldFailValidation_whenEmailIsInvalid() {
    var dto =
        new RegisterRequestDto(
            "invalid-email",
            validDto().getPassword(),
            validDto().getFirstName(),
            validDto().getLastName(),
            validDto().getPhoneNumber(),
            validDto().getBirthDate(),
            validDto().getRole());

    assertViolation(dto, "email");
  }

  @Test
  void shouldFailValidation_whenPasswordIsBlank() {
    var dto =
        new RegisterRequestDto(
            validDto().getEmail(),
            "",
            validDto().getFirstName(),
            validDto().getLastName(),
            validDto().getPhoneNumber(),
            validDto().getBirthDate(),
            validDto().getRole());

    assertViolation(dto, "password");
  }

  @Test
  void shouldFailValidation_whenPasswordIsWeak() {
    var dto =
        new RegisterRequestDto(
            validDto().getEmail(),
            "password",
            validDto().getFirstName(),
            validDto().getLastName(),
            validDto().getPhoneNumber(),
            validDto().getBirthDate(),
            validDto().getRole());

    assertViolation(dto, "password");
  }

  @Test
  void shouldFailValidation_whenFirstNameIsBlank() {
    var dto =
        new RegisterRequestDto(
            validDto().getEmail(),
            validDto().getPassword(),
            "",
            validDto().getLastName(),
            validDto().getPhoneNumber(),
            validDto().getBirthDate(),
            validDto().getRole());

    assertViolation(dto, "firstName");
  }

  @Test
  void shouldFailValidation_whenLastNameIsBlank() {
    var dto =
        new RegisterRequestDto(
            validDto().getEmail(),
            validDto().getPassword(),
            validDto().getFirstName(),
            "",
            validDto().getPhoneNumber(),
            validDto().getBirthDate(),
            validDto().getRole());

    assertViolation(dto, "lastName");
  }

  @Test
  void shouldFailValidation_whenPhoneNumberIsBlank() {
    var dto =
        new RegisterRequestDto(
            validDto().getEmail(),
            validDto().getPassword(),
            validDto().getFirstName(),
            validDto().getLastName(),
            "",
            validDto().getBirthDate(),
            validDto().getRole());

    assertViolation(dto, "phoneNumber");
  }

  @Test
  void shouldFailValidation_whenPhoneNumberIsInvalid() {
    var dto =
        new RegisterRequestDto(
            validDto().getEmail(),
            validDto().getPassword(),
            validDto().getFirstName(),
            validDto().getLastName(),
            "abc123",
            validDto().getBirthDate(),
            validDto().getRole());

    assertViolation(dto, "phoneNumber");
  }

  @Test
  void shouldFailValidation_whenBirthDateIsBlank() {
    var dto =
        new RegisterRequestDto(
            validDto().getEmail(),
            validDto().getPassword(),
            validDto().getFirstName(),
            validDto().getLastName(),
            validDto().getPhoneNumber(),
            "",
            validDto().getRole());

    assertViolation(dto, "birthDate");
  }

  @Test
  void shouldFailValidation_whenBirthDateIsInvalidFormat() {
    var dto =
        new RegisterRequestDto(
            validDto().getEmail(),
            validDto().getPassword(),
            validDto().getFirstName(),
            validDto().getLastName(),
            validDto().getPhoneNumber(),
            "2024/01/01",
            validDto().getRole() // Mauvais format
            );

    assertViolation(dto, "birthDate");
  }

  @Test
  void shouldFailValidation_whenRoleIsBlank() {
    var dto =
        new RegisterRequestDto(
            validDto().getEmail(),
            validDto().getPassword(),
            validDto().getFirstName(),
            validDto().getLastName(),
            validDto().getPhoneNumber(),
            validDto().getBirthDate(),
            "");

    assertViolation(dto, "role");
  }

  @Test
  void shouldFailValidation_whenRoleIsInvalid() {
    var dto =
        new RegisterRequestDto(
            validDto().getEmail(),
            validDto().getPassword(),
            validDto().getFirstName(),
            validDto().getLastName(),
            validDto().getPhoneNumber(),
            validDto().getBirthDate(),
            "INVALID_ROLE");

    assertViolation(dto, "role");
  }

  private void assertViolation(RegisterRequestDto dto, String fieldName) {
    Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);
    assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals(fieldName));
  }
}
