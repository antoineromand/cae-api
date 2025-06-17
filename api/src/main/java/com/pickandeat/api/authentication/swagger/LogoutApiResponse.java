package com.pickandeat.api.authentication.swagger;

import com.pickandeat.api.shared.GenericApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LogoutApiResponse", description = "Successful response after logout")
public class LogoutApiResponse extends GenericApiResponse<Void> {
    public LogoutApiResponse(String message) {
        super(message, null);
    }
}
