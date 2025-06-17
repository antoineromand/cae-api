package com.pickandeat.api.authentication.swagger;

import java.util.UUID;

import com.pickandeat.api.shared.GenericApiResponse;

public class RegisterApiResponse extends GenericApiResponse<UUID> {
    public RegisterApiResponse() {
        super("Registration completed successfully.", null);
    }
}
