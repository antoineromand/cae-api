package com.pickandeat.authentication.application.usecase.register;

import com.pickandeat.authentication.application.exceptions.application.EmailAlreadyUsedException;
import com.pickandeat.authentication.application.exceptions.technical.CannotHashPasswordException;
import com.pickandeat.authentication.application.exceptions.technical.DatabaseTechnicalException;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.repository.ICredentialsRepository;
import com.pickandeat.authentication.domain.service.IPasswordService;
import com.pickandeat.authentication.domain.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("unit")
public class RegisterUseCaseUnitTest {
    private ICredentialsRepository credentialsRepository;

    private RegisterUseCase registerUseCase;

    private IPasswordService passwordService;

    private RegisterCommand getCommand() {
        String dateString = "2025-05-24";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new RegisterCommand(
                "test@gmail.com",
                "clearPassword",
                "jerome",
                "juda",
                "+33650333340",
                LocalDate.parse(dateString, formatter),
                new Role(RoleName.CONSUMER, null));
    }

    @BeforeEach
    void init() {
        this.credentialsRepository = mock(ICredentialsRepository.class);
        this.passwordService = mock(IPasswordService.class);
        this.registerUseCase = new RegisterUseCase(credentialsRepository, passwordService);
    }

    @Test
    public void register_shouldThrowEmailAlreadyUsedException_whenEmailIsAlreadyTaken() {
        RegisterCommand command = getCommand();

        Credentials existingCredentials = mock(Credentials.class);
        when(credentialsRepository.findByEmail(command.email()))
                .thenReturn(Optional.of(existingCredentials));

        assertThrows(EmailAlreadyUsedException.class, () -> registerUseCase.execute(command));
    }

    @Test
    public void register_shouldThrowCannotHashPasswordException_whenPasswordHashingFails() {
        RegisterCommand command = getCommand();

        when(credentialsRepository.findByEmail(command.email())).thenReturn(Optional.empty());

        RuntimeException rootCause = new RuntimeException("Hashing failed");

        when(passwordService.hashPassword(command.password())).thenThrow(rootCause);

        assertThrows(CannotHashPasswordException.class, () -> registerUseCase.execute(command));
    }

    @Test
    public void register_shouldThrowRegistrationTechnicalException_whenDatabaseSaveFails() {
        RegisterCommand command = getCommand();

        when(credentialsRepository.findByEmail(command.email())).thenReturn(Optional.empty());

        String hashPassword = "hashPassword";

        when(passwordService.hashPassword(command.password())).thenReturn(hashPassword);

        when(credentialsRepository.save(any(Credentials.class)))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DatabaseTechnicalException.class, () -> registerUseCase.execute(command));
    }

    @Test
    public void register_shouldReturnUserId_whenRegistrationSucceeds() {
        RegisterCommand command = getCommand();

        when(credentialsRepository.findByEmail(command.email())).thenReturn(Optional.empty());

        String hashPassword = "hashPassword";

        when(passwordService.hashPassword(command.password())).thenReturn(hashPassword);

        UUID credentialsId = UUID.randomUUID();

        when(credentialsRepository.save(any(Credentials.class))).thenReturn(credentialsId);

        UUID result = registerUseCase.execute(command);

        assertEquals(credentialsId, result);
    }
}
