package com.pickandeat.authentication.application.exceptions.application;

public class RoleMismatchException extends AbstractApplicationException {
  public RoleMismatchException() {
    super(
        "ROLE_MISMATCH",
        "The user's role does not match the requested authentication context.",
        null);
  }
}
