package com.pickandeat.authentication.application.usecase.login;

import static org.junit.Assert.assertThrows;

import com.pickandeat.authentication.application.TokenPair;
import com.pickandeat.authentication.application.exceptions.application.EmailNotFoundException;
import com.pickandeat.authentication.application.exceptions.application.PasswordNotMatchException;
import com.pickandeat.authentication.application.usecase.register.RegisterCommand;
import com.pickandeat.authentication.application.usecase.register.RegisterUseCase;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.authentication.infrastructure.database.AbstractDatabaseContainersTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Tag("functional")
public class LoginUseCaseFunctionalTest extends AbstractDatabaseContainersTest {
    @Autowired
    LoginUseCase loginUseCase;

    @Autowired
    RegisterUseCase registerUseCase;

    private void createCredentials() {
        String dateString = "2025-05-24";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        RegisterCommand command =
                new RegisterCommand(
                        "test@test.com",
                        "MotDePasseTest06?",
                        "john",
                        "doe",
                        "+33650333340",
                        LocalDate.parse(dateString, formatter),
                        new Role(RoleName.CONSUMER, null));
        this.registerUseCase.execute(command);
    }

    @BeforeAll
    void init() {
        this.createCredentials();
    }

    @Test
    void login_shouldThrowUserNotFoundException_whenEmailDoesNotExist() {
        LoginCommand loginCommand = new LoginCommand("not-a-user@email.com", "LePoissonSteve?2");

        assertThrows(EmailNotFoundException.class, () -> this.loginUseCase.execute(loginCommand));
    }

    @Test
    @Transactional
    void login_shouldThrowPasswordNotMatchException_whenPasswordIsIncorrect() {
        LoginCommand loginCommand = new LoginCommand("test@test.com", "MauvaisMotDePasse33?");

        assertThrows(PasswordNotMatchException.class, () -> this.loginUseCase.execute(loginCommand));
    }

    @Test
    @Transactional
    void login_shouldReturnToken_whenCredentialsAreValid() {
        LoginCommand loginCommand = new LoginCommand("test@test.com", "MotDePasseTest06?");

        TokenPair resultToken = this.loginUseCase.execute(loginCommand);

        Assertions.assertFalse(resultToken.getAccessToken().isBlank());
        Assertions.assertFalse(resultToken.getRefreshToken().isBlank());
    }
}
