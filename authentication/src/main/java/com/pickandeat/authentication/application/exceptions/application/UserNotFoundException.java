package com.pickandeat.authentication.application.exceptions.application;

public class UserNotFoundException extends AbstractApplicationException {
    public UserNotFoundException(Throwable cause) {
        super("USER_NOT_FOUND", "User does not exist.", cause);
    }

    public UserNotFoundException() {
        super("USER_NOT_FOUND", "User does not exist.", null);
    }
}
