package com.pickandeat.authentication.application.exceptions;

public class JtiNotFoundInCacheException extends RuntimeException{
    public JtiNotFoundInCacheException(String message) {
        super(message);
    }
}
