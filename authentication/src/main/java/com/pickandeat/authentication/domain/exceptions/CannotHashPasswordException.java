package com.pickandeat.authentication.domain.exceptions;

public class CannotHashPasswordException extends RuntimeException {
    public CannotHashPasswordException(Throwable throwable) {
        super("Error while hashing password: " + throwable);
    }
}
