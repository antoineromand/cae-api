package com.pickandeat.authentication.application.exceptions.application;

public class AbstractApplicationException extends RuntimeException {
    private final String key;

    public AbstractApplicationException(String key, String message, Throwable cause) {
        super(message, cause);
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
