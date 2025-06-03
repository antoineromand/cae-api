package com.pickandeat.authentication.application.exceptions;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super(email + " exists. Please use another email.");
    }

}
