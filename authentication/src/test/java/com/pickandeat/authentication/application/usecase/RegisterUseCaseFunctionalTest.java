package com.pickandeat.authentication.application.usecase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.pickandeat.authentication.TestConfiguration;
import com.pickandeat.authentication.application.exceptions.EmailAlreadyUsedException;
import com.pickandeat.authentication.application.usecase.register.RegisterCommand;
import com.pickandeat.authentication.application.usecase.register.RegisterUseCase;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.authentication.infrastructure.database.AbstractDatabaseContainersTest;

import jakarta.transaction.Transactional;

@SpringBootTest(classes = TestConfiguration.class)
@Testcontainers
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
    @Transactional
    void shouldRegisterSuccessfully() {
        RegisterCommand command = getCommand("unique-user@example.com");
        UUID result = registerUseCase.register(command);
        assertNotNull(result, "Le UUID retourné ne doit pas être null");
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenEmailAlreadyUsed() {
        String email = "duplicate@example.com";
        RegisterCommand first = getCommand(email);
        registerUseCase.register(first);

        RegisterCommand second = getCommand(email);
        assertThrows(EmailAlreadyUsedException.class, () -> registerUseCase.register(second));
    }

    @Test
    @Transactional
    void shouldReturnDifferentIdsForDifferentUsers() {
        RegisterCommand one = getCommand("user1@example.com");
        RegisterCommand two = getCommand("user2@example.com");

        UUID id1 = registerUseCase.register(one);
        UUID id2 = registerUseCase.register(two);

        assertNotEquals(id1, id2, "Deux utilisateurs différents ne doivent pas avoir le même UUID");
    }

}
