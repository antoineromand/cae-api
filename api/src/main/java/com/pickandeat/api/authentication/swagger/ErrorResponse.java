package com.pickandeat.api.authentication.swagger;

import com.pickandeat.api.shared.ErrorApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(name = "ErrorResponse", description = "Standardized error response")
public class ErrorResponse extends ErrorApiResponse {
    public ErrorResponse() {
        super("ERROR_CODE", "error message", 400, null);
    }
}
