package com.pickandeat.authentication.application.exceptions;

public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException() {
        super("Given password does not match with user password.");
    }
}
