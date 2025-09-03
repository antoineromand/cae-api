package com.clickandeat.api.authentication.swagger;

import com.clickandeat.api.shared.GenericApiResponse;
import java.util.UUID;

public class RegisterApiResponse extends GenericApiResponse<UUID> {
  public RegisterApiResponse() {
    super("Registration completed successfully.", UUID.randomUUID());
  }
}
