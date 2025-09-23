package com.clickandeat.account.infrastructure.repository;

import com.clickandeat.account.domain.repository.IAccountRepository;
import org.springframework.stereotype.Component;

@Component
public class AccountRepositoryImpl implements IAccountRepository {
  private final AccountJpaRepository accountJpaRepository;

  public AccountRepositoryImpl(AccountJpaRepository accountJpaRepository) {
    this.accountJpaRepository = accountJpaRepository;
  }

  @Override
  public boolean isPhoneNumberUnique(String phoneNumber) {
    return this.accountJpaRepository.existsAccountEntityByPhoneNumber(phoneNumber);
  }
}
