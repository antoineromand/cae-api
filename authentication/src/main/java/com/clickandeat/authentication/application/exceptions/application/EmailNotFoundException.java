package com.clickandeat.authentication.application.exceptions.application;

public class EmailNotFoundException extends AbstractApplicationException {
  public EmailNotFoundException() {
    super("INVALID_CREDENTIALS", "Email not registered in database.", null);
  }
}
