package com.pickandeat.api.authentication.swagger;

import com.pickandeat.api.shared.GenericApiResponse;
import com.pickandeat.authentication.application.TokenPair;

public class LoginApiResponse extends GenericApiResponse<TokenPair> {
    public LoginApiResponse() {
        super("Authentication successful.", new TokenPair("accessTokenSample", "refreshTokenSample"));
    }
}
