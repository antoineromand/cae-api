package com.pickandeat.authentication.application.exceptions.technical;

public class CannotHashPasswordException extends AbstractTechnicalException {
  public CannotHashPasswordException(Throwable throwable) {
    super("password-service", "Cannot hash the given password.", throwable);
  }
}
