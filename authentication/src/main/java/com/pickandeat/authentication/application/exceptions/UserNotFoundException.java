package com.pickandeat.authentication.application.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String credentials) {
        super(credentials + " not found.");
    }
}
