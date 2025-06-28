package com.pickandeat.api.authentication.swagger;

import com.pickandeat.api.shared.GenericApiResponse;
import com.pickandeat.authentication.application.TokenPair;

public class UpdatePasswordApiResponse extends GenericApiResponse<TokenPair> {
    public UpdatePasswordApiResponse() {
        super(
                "Update password response example",
                new TokenPair("accessTokenSample", "refreshTokenSample"));
    }
}
