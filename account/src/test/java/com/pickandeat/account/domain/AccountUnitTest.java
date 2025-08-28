package com.pickandeat.account.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.pickandeat.account.domain.account.Account;
import com.pickandeat.account.domain.account.AccountPhoneNumber;
import com.pickandeat.account.domain.account.pro.AccountProInformations;
import com.pickandeat.shared.enums.RoleName;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
public class AccountUnitTest {
  com.pickandeat.account.domain.account.pro.AccountProInformations accountProInformations =
      new AccountProInformations(
              2L,
          "ref",
          "siret",
          "deluxe kebab",
          "SARL",
          "103 Avenue des potirons",
          null,
          null,
          "Narnia",
          "010101",
          "France");

  private final Account consumerAccount =
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

  private final Account proAccount =
      new Account(
          2L,
          "Doe",
          "John",
          RoleName.PRO,
          "+33650333125",
          "test@gmail.com",
          "1995-09-04",
          Date.from(Instant.now()),
          null,
          accountProInformations);

  @Test
  public void testConstructor() {
    assertEquals("Doe", consumerAccount.getLastName());
    assertEquals("John", consumerAccount.getFirstName());
    assertEquals(RoleName.CONSUMER, consumerAccount.getRole());
  }

  @Test
  public void testAccountBirthDate_shouldGetAge() throws ParseException {
    int actualAge = 29;

    assertEquals(actualAge, consumerAccount.getAccountBirthDate().getAge());
  }

  @Test
  public void testAccountPhoneNumber_shouldGetPhoneNumber() {
    assertEquals("+33650333125", consumerAccount.getAccountPhoneNumber().phoneNumber());
  }

  @Test
  public void testAccountPhoneNumber_shouldThrowException() {
    assertThrows(IllegalArgumentException.class, () -> new AccountPhoneNumber("xce33650333125"));
  }

  @Test
  public void testProInformations_shouldGetFullLocalisation() {
    String fullAddress = "103 Avenue des potirons, Narnia, 010101, France";

    assertEquals(fullAddress, this.proAccount.getAccountProInformations()
            .getLocalisation().fullAddress());
  }

  @Test
  public void testProInformationsId_shouldHaveSameThanAccountId() {
    assertEquals(this.proAccount.getId(), this.accountProInformations.getId());
  }
}
