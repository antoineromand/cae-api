package com.pickandeat.authentication.application.exceptions;

public class InvalidUserIdInRefreshToken extends RuntimeException {
    public InvalidUserIdInRefreshToken(String message) {
        super(message);
    }
}
