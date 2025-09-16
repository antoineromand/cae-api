package com.clickandeat.account.infrastructure.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.clickandeat.account.domain.account.Account;
import com.clickandeat.account.domain.account.pro.AccountProInformations;
import com.clickandeat.shared.enums.RoleName;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
public class AccountProInformationsUnitTest {
  private static final AccountProInformations PRO_ACCOUNT_INFOS =
      new AccountProInformations(
          1L,
          null,
          "78467169500087",
          "Deluxe Kebab Antibes",
          "SARL",
          "10 Avenue de provence",
          null,
          null,
          "Antibes",
          "06600",
          "France");

  private static final Account ACCOUNT_DOMAIN =
      new Account(
          1L,
          "Doe",
          "John",
          RoleName.CONSUMER,
          "+33650333125",
          "test@gmail.com",
          "1995-09-04",
          Date.from(Instant.now()),
          null,
          PRO_ACCOUNT_INFOS);

  private static final UUID ACCOUNT_ID = UUID.randomUUID();

  @Test()
  public void shouldConvertToEntityClass() {
    AccountProInformationsEntity accountProInformations =
        AccountProInformationsEntity.fromDomain(
            PRO_ACCOUNT_INFOS,
            AccountEntity.fromDomain(ACCOUNT_DOMAIN, UUID.randomUUID()),
            ACCOUNT_ID);

    assertEquals(1L, accountProInformations.getId());

    AccountProInformations toDomain = accountProInformations.toDomain();

    assertEquals(PRO_ACCOUNT_INFOS.getSiret(), toDomain.getSiret());
  }
}
