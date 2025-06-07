package com.pickandeat.authentication.application.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super(email + " not found. Please use another email.");
    }
}
