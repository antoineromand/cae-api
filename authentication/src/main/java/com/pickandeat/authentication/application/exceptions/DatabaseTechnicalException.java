package com.pickandeat.authentication.application.exceptions;

public class DatabaseTechnicalException extends RuntimeException {
    public DatabaseTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }
}
