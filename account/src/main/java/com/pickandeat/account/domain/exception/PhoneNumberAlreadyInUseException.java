package com.pickandeat.account.domain.exception;

public class PhoneNumberAlreadyInUseException extends AbstractDomainException {
    public PhoneNumberAlreadyInUseException() {
        super("PHONE_NUMBER_ALREADY_IN_USE","Phone number is already in use");
    }
}
