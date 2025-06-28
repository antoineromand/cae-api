package com.pickandeat.authentication.application.exceptions.application;

public class EmailAlreadyUsedException extends AbstractApplicationException {

  public EmailAlreadyUsedException(Throwable cause) {
    super("EMAIL_ALREADY_USED", "Email is already registered.", cause);
  }

  public EmailAlreadyUsedException() {
    super("EMAIL_ALREADY_USED", "Email is already registered.", null);
  }
}
