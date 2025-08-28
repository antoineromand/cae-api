package com.pickandeat.account.domain.repository;

public interface IAccountRepository {
    boolean isPhoneNumberUnique(String phoneNumber);
}
