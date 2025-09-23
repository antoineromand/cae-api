package com.clickandeat.account.domain.repository;

public interface IAccountRepository {
  boolean isPhoneNumberUnique(String phoneNumber);
}
