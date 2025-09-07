package com.clickandeat.account.infrastructure.repository;

import com.clickandeat.account.infrastructure.database.AbstractDatabaseContainersTest;
import com.clickandeat.account.infrastructure.model.AccountEntity;
import com.clickandeat.shared.enums.RoleName;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
public class AccountRepositoryImplIntegrationTest extends AbstractDatabaseContainersTest {
    @Autowired private AccountJpaRepository accountJpaRepository;
    @Autowired private AccountRepositoryImpl accountRepository;

    @Test
    public void isPhoneNumberUnique_shouldReturnTrueIfItExistsOrElseFalse() {
        AccountEntity accountEntity = new AccountEntity(
                null,
                "John",
                "Doe",
                UUID.randomUUID(),
                LocalDate.parse("1995-09-04"),
                "+33650505050",
                Instant.now(),
                null,
                RoleName.CONSUMER
        );

        this.accountJpaRepository.save(accountEntity);

        String falsePhoneNumber = "+33640404040";

        assertTrue(this.accountRepository.isPhoneNumberUnique("+33650505050"));
        assertFalse(this.accountRepository.isPhoneNumberUnique(falsePhoneNumber));
    }
}
