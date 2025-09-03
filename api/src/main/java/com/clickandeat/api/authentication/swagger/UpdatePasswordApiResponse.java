package com.clickandeat.api.authentication.swagger;

import com.clickandeat.api.shared.GenericApiResponse;
import com.clickandeat.authentication.application.TokenPair;

public class UpdatePasswordApiResponse extends GenericApiResponse<TokenPair> {
  public UpdatePasswordApiResponse() {
    super(
        "Update password response example",
        new TokenPair("accessTokenSample", "refreshTokenSample"));
  }
}
