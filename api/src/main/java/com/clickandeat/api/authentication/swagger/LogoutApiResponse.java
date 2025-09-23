package com.clickandeat.api.authentication.swagger;

import com.clickandeat.api.shared.GenericApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LogoutApiResponse", description = "Successful response after logout")
public class LogoutApiResponse extends GenericApiResponse<Void> {
  public LogoutApiResponse() {
    super("Logout message example", null);
  }
}
