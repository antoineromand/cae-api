package com.pickandeat.authentication.application.exceptions.application;

public class EmailAlreadyUsedException extends AbstractApplicationException {

  public EmailAlreadyUsedException() {
    super("EMAIL_ALREADY_USED", "Email is already registered.", null);
  }
}
