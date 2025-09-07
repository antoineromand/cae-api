package com.clickandeat.account.infrastructure.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.clickandeat.account.domain.account.Account;
import com.clickandeat.shared.enums.RoleName;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
public class AccountEntityUnitTest {
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
          null);
  private static final AccountEntity ACCOUNT_ENTITY =
      new AccountEntity(
          1L, "Tony", "Stark", UUID.randomUUID(), LocalDate.parse("1995-08-04"), "+33650121314", Instant.now(), null, RoleName.CONSUMER);

  @Test
  public void shouldConvertAccountEntityToAccount() {
    Account account = ACCOUNT_ENTITY.toDomain("tony.stark@marvel.com");
    assertEquals("Tony", account.getFirstName());
    assertEquals("tony.stark@marvel.com", account.getAccountEmail());
  }

  @Test
  public void shouldConvertAccountDomainToAccountEntity() {
    AccountEntity accountEntity = AccountEntity.fromDomain(ACCOUNT_DOMAIN, UUID.randomUUID());
    assertEquals(1L, accountEntity.getId());
  }
}
