package com.pickandeat.authentication.application.usecase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.dao.DataIntegrityViolationException;

import com.pickandeat.authentication.application.exceptions.EmailAlreadyUsedException;
import com.pickandeat.authentication.application.exceptions.RegistrationTechnicalException;
import com.pickandeat.authentication.application.usecase.register.RegisterCommand;
import com.pickandeat.authentication.application.usecase.register.RegisterUseCase;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.exceptions.CannotHashPasswordException;
import com.pickandeat.authentication.domain.repository.ICredentialsRespository;
import com.pickandeat.authentication.domain.service.IPasswordService;
import com.pickandeat.authentication.domain.valueobject.Role;

public class RegisterUseCaseUnitTest {
        private ICredentialsRespository credentialsRespository;

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
                this.credentialsRespository = mock(ICredentialsRespository.class);
                this.passwordService = mock(IPasswordService.class);
                this.registerUseCase = new RegisterUseCase(credentialsRespository, passwordService);
        }

        @Test
        public void shouldNotRegisterWithAnUsedEmail() {
                RegisterCommand command = getCommand();

                Credentials existingCredentials = mock(Credentials.class);
                when(credentialsRespository.findByEmail(command.email()))
                                .thenReturn(Optional.of(existingCredentials));

                assertThrows(EmailAlreadyUsedException.class, () -> registerUseCase.register(command));
        }

        @Test
        public void shouldNotRegisterIfErrorWhileHashingPassword() {
                RegisterCommand command = getCommand();

                when(credentialsRespository.findByEmail(command.email()))
                                .thenReturn(Optional.empty());

                RuntimeException rootCause = new RuntimeException("Hashing failed");

                when(passwordService.hashPassword(command.password())).thenThrow(rootCause);

                assertThrows(
                                CannotHashPasswordException.class,
                                () -> registerUseCase.register(command));
        }

        @Test
        public void shouldNotRegisterIfErrorWhileSavingToDB() {
                RegisterCommand command = getCommand();

                when(credentialsRespository.findByEmail(command.email()))
                                .thenReturn(Optional.empty());

                String hashPassword = "hashPassword";

                when(passwordService.hashPassword(command.password())).thenReturn(hashPassword);

                when(credentialsRespository.save(any(Credentials.class)))
                                .thenThrow(DataIntegrityViolationException.class);

                assertThrows(
                                RegistrationTechnicalException.class,
                                () -> registerUseCase.register(command));
        }

        @Test
        public void shouldRegisterUserAndReturnID() {
                RegisterCommand command = getCommand();

                when(credentialsRespository.findByEmail(command.email()))
                                .thenReturn(Optional.empty());

                String hashPassword = "hashPassword";

                when(passwordService.hashPassword(command.password())).thenReturn(hashPassword);

                UUID credentialsId = UUID.randomUUID();

                when(credentialsRespository.save(any(Credentials.class))).thenReturn(credentialsId);

                UUID result = registerUseCase.register(command);

                assertEquals(credentialsId, result);

        }
}
