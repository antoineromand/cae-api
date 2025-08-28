package com.pickandeat.account.domain.exception;

public class AbstractDomainException extends RuntimeException {
    private String code;
    public AbstractDomainException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
