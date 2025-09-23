package com.clickandeat.authentication.application.exceptions.application;

public class JtiNotFoundInCacheException extends AbstractApplicationException {

  public JtiNotFoundInCacheException() {
    super("JTI_NOT_FOUND", "Jeton token id not found in Cache", null);
  }
}
