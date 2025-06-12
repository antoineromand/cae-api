package com.pickandeat.api.authentication.swagger;

import com.pickandeat.api.shared.GenericApiResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorResponse", description = "Standardized error response")
public class ErrorResponse extends GenericApiResponse<String> {
    public ErrorResponse() {
        super("Detailed error message here", null);
    }
}
