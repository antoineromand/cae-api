package com.pickandeat.api.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginRequestDto {
  @Email(message = "email form not valid.")
  @NotBlank(message = "email must be provided.")
  @Schema(description = "User's email", example = "example@example.com")
  private final String email;

  @NotBlank(message = "password must be provided.")
  @Size(min = 8)
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
      message =
          "password must contain at least 8 characters, including a lowercase letter, an uppercase letter, a digit, and a special character.")
  @Schema(description = "User's password", example = "AstrongPassw0rd!")
  private final String password;

  public LoginRequestDto(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }
}
