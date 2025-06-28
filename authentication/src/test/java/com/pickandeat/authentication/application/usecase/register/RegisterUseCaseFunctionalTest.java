package com.pickandeat.authentication.application.usecase.register;

import com.pickandeat.authentication.application.exceptions.application.EmailAlreadyUsedException;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.authentication.infrastructure.database.AbstractDatabaseContainersTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("functional")
@Transactional
public class RegisterUseCaseFunctionalTest extends AbstractDatabaseContainersTest {
    @Autowired
    private RegisterUseCase registerUseCase;

    private RegisterCommand getCommand(String email) {
        String dateString = "2025-05-24";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new RegisterCommand(
                email,
                "clearPassword",
                "jerome",
                "juda",
                "+33650333340",
                LocalDate.parse(dateString, formatter),
                new Role(RoleName.CONSUMER, null));
    }

    @Test
    void register_shouldSucceed_whenEmailIsUnique() {
        RegisterCommand command = getCommand("unique-user@example.com");
        UUID result = registerUseCase.execute(command);
        assertNotNull(result, "Le UUID retourné ne doit pas être null");
    }

    @Test
    void register_shouldThrowEmailAlreadyUsedException_whenEmailAlreadyExists() {
        String email = "duplicate@example.com";
        RegisterCommand first = getCommand(email);
        registerUseCase.execute(first);

        RegisterCommand second = getCommand(email);
        assertThrows(EmailAlreadyUsedException.class, () -> registerUseCase.execute(second));
    }

    @Test
    void register_shouldReturnDifferentIds_whenRegisteringTwoDifferentUsers() {
        RegisterCommand one = getCommand("user1@example.com");
        RegisterCommand two = getCommand("user2@example.com");

        UUID id1 = registerUseCase.execute(one);
        UUID id2 = registerUseCase.execute(two);

        assertNotEquals(id1, id2, "Deux utilisateurs différents ne doivent pas avoir le même UUID");
    }
}
