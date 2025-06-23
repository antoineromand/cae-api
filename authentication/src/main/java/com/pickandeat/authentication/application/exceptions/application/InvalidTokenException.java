package com.pickandeat.authentication.application.exceptions.application;

public class InvalidTokenException extends AbstractApplicationException {
    public InvalidTokenException() {
        super("INVALID_TOKEN", "Token is not valid.", null);
    }
}
