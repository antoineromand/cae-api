package com.pickandeat.authentication.application.exceptions.technical;

public class DatabaseTechnicalException extends AbstractTechnicalException {
    public DatabaseTechnicalException(String message, Throwable cause) {
        super("database", message, cause);
    }
}
