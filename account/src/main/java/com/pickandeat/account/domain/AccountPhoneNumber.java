package com.pickandeat.account.domain;

import java.util.regex.Pattern;

public record AccountPhoneNumber(String phoneNumber) {
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");

    public AccountPhoneNumber {
        if (phoneNumber == null) {
            throw new IllegalArgumentException("Phone number is required");
        }
        String normalized = phoneNumber.trim();
        if (!PHONE_NUMBER_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Invalid phone number: " + phoneNumber);
        }
        phoneNumber = normalized;
    }
}
