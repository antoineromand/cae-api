package com.pickandeat.api.authentication.swagger;

import java.util.UUID;

import com.pickandeat.api.shared.GenericApiResponse;

public class RegisterApiResponse extends GenericApiResponse<UUID> {
    public RegisterApiResponse() {
        super("Registration completed successfully.", UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    }
}
