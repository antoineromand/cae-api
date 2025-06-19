package com.pickandeat.authentication.application.usecase.update_password;

import java.util.UUID;

public record UpdatePasswordCommand(UUID userId, String oldPassword, String newPassword) {
}
