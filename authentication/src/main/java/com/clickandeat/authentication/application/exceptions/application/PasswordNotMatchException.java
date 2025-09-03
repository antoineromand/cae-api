package com.clickandeat.authentication.application.exceptions.application;

public class PasswordNotMatchException extends AbstractApplicationException {
  public PasswordNotMatchException() {
    super("INVALID_CREDENTIALS", "Given password does not match with user password.", null);
  }
}
