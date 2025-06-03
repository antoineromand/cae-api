package com.pickandeat.authentication.application.exceptions;

public class RegistrationTechnicalException extends RuntimeException {
    public RegistrationTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }
}
