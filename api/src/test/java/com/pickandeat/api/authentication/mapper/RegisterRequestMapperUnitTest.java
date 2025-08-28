package com.pickandeat.api.authentication.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.pickandeat.api.authentication.dto.RegisterRequestDto;
import com.pickandeat.authentication.application.usecase.register.RegisterCommand;
import com.pickandeat.shared.enums.RoleName;
import java.time.LocalDate;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
public class RegisterRequestMapperUnitTest {

  @Test
  void shouldMapRegisterRequestDtoToRegisterCommand() {
    RegisterRequestDto dto =
        new RegisterRequestDto(
            "test@example.com",
            "securePass123",
            "John",
            "Doe",
            "0123456789",
            "2000-01-15",
            "CONSUMER");

    RegisterCommand command = RegisterRequestMapper.toCommand(dto);

    assertEquals("test@example.com", command.email());
    assertEquals("securePass123", command.password());
    assertEquals("John", command.firstName());
    assertEquals("Doe", command.lastName());
    assertEquals("0123456789", command.phoneNumber());
    assertEquals(LocalDate.of(2000, 1, 15), command.birthDate());
    assertEquals(RoleName.CONSUMER, command.role().name());
  }

  @Test
  void shouldThrowExceptionForInvalidRole() {
    RegisterRequestDto dto =
        new RegisterRequestDto(
            "test@example.com",
            "securePass123",
            "John",
            "Doe",
            "0123456789",
            "2000-01-15",
            "not_a_real_role");

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          RegisterRequestMapper.toCommand(dto);
        });
  }

  @Test
  void shouldThrowExceptionForInvalidDate() {
    RegisterRequestDto dto =
        new RegisterRequestDto(
            "test@example.com",
            "securePass123",
            "John",
            "Doe",
            "0123456789",
            "not-a-date",
            "CONSUMER");

    assertThrows(
        Exception.class,
        () -> {
          RegisterRequestMapper.toCommand(dto);
        });
  }
}
