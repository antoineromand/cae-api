package com.clickandeat.account.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.clickandeat.account.domain.account.Account;
import com.clickandeat.account.domain.account.pro.AccountProInformations;
import com.clickandeat.account.infrastructure.database.AbstractDatabaseContainersTest;
import com.clickandeat.account.infrastructure.model.AccountEntity;
import com.clickandeat.account.infrastructure.model.AccountProInformationsEntity;
import com.clickandeat.shared.enums.RoleName;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("integration")
public class AccountProInformationsJpaIntegrationTest extends AbstractDatabaseContainersTest {
  @Autowired private AccountProInformationsJpaRepository accountProInformationsJpaRepository;
  @Autowired private AccountJpaRepository accountJpaRepository;

  @Test
  @Transactional
  public void shouldSaveAccountProInformations() {
    AccountProInformations accountProInformations =
        new AccountProInformations(
            null,
            null,
            "78467169500087",
            "Test 1",
            "SARL",
            "40 Avenue de Provence",
            null,
            null,
            "Antibes",
            "06600",
            "France");

    Account account =
        new Account(
            null,
            "Jiji",
            "Jojo",
            RoleName.CONSUMER,
            "+33650333125",
            "jiji-jojo@gmail.com",
            "1995-09-04",
            Date.from(Instant.now()),
            null,
            null);

    UUID credentialsId = UUID.randomUUID();

    AccountEntity accountEntity =
        this.accountJpaRepository.save(AccountEntity.fromDomain(account, credentialsId));

    Long accountId = accountEntity.getId();

    AccountProInformationsEntity accountProInformationsEntity =
        accountProInformationsJpaRepository.save(
            AccountProInformationsEntity.fromDomain(
                accountProInformations, accountEntity, credentialsId));

    assertEquals(accountId, accountProInformationsEntity.toDomain().getId());
  }
}
