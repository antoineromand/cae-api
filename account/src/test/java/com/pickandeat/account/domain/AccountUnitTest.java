package com.pickandeat.account.domain;

import com.pickandeat.shared.enums.RoleName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Tag("unit")
public class AccountUnitTest {
    Account consumerAccount = new Account(
            1L,
            "Doe",
            "John",
            RoleName.CONSUMER,
            "+33650333125",
            "test@gmail.com",
            "1995-09-04",
            Date.from(Instant.now()),
            null,
            null
    );

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

}
