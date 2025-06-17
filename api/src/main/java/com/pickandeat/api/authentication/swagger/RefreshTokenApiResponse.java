package com.pickandeat.api.authentication.swagger;

import com.pickandeat.api.shared.GenericApiResponse;
import com.pickandeat.authentication.application.usecase.login.Token;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RefreshTokenApiResponse", description = "Successful response after refreshing tokens")
public class RefreshTokenApiResponse extends GenericApiResponse<Token> {
    public RefreshTokenApiResponse(String message, Token data) {
        super(message, data);
    }
}
