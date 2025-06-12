package com.pickandeat.api.authentication.swagger;

import com.pickandeat.api.shared.GenericApiResponse;
import com.pickandeat.authentication.application.usecase.login.Token;

public class LoginApiResponse extends GenericApiResponse<Token> {
    public LoginApiResponse() {
        super("Authentication successful.", new Token("accessTokenSample", "refreshTokenSample"));
    }
}
