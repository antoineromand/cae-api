package com.pickandeat.api.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdatePasswordRequestDto {
    @NotBlank(message = "password must be provided.")
    @Size(min = 8)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message =
                    "password must contain at least 8 characters, including a lowercase letter, an uppercase letter, a digit, and a special character.")
    @Schema(description = "User actual password", example = "AOldstrongPassw0rd!")
    private final String oldPassword;

    @NotBlank(message = "password must be provided.")
    @Size(min = 8)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message =
                    "password must contain at least 8 characters, including a lowercase letter, an uppercase letter, a digit, and a special character.")
    @Schema(description = "User new password", example = "ANewstrongPassw0rd!")
    private final String newPassword;

    public UpdatePasswordRequestDto(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return this.oldPassword;
    }

    public String getNewPassword() {
        return this.newPassword;
    }
}
