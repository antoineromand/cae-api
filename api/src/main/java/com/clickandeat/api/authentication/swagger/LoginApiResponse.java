package com.clickandeat.api.authentication.swagger;

import com.clickandeat.api.shared.GenericApiResponse;
import com.clickandeat.authentication.application.TokenPair;

public class LoginApiResponse extends GenericApiResponse<TokenPair> {
  public LoginApiResponse() {
    super("Authentication successful.", new TokenPair("accessTokenSample", "refreshTokenSample"));
  }
}
