package com.clickandeat.api.authentication.swagger;

import com.clickandeat.api.shared.GenericApiResponse;
import com.clickandeat.authentication.application.TokenPair;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    name = "RefreshTokenApiResponse",
    description = "Successful response after refreshing tokens")
public class RefreshTokenApiResponse extends GenericApiResponse<TokenPair> {
  public RefreshTokenApiResponse() {
    super(
        "Refresh token message example", new TokenPair("accessTokenSample", "refreshTokenSample"));
  }
}
