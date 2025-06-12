package com.pickandeat.authentication.application.usecase.login;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.pickandeat.authentication.TestConfiguration;
import com.pickandeat.authentication.application.exceptions.PasswordNotMatchException;
import com.pickandeat.authentication.application.exceptions.UserNotFoundException;
import com.pickandeat.authentication.application.usecase.register.RegisterCommand;
import com.pickandeat.authentication.application.usecase.register.RegisterUseCase;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.authentication.infrastructure.database.AbstractDatabaseContainersTest;

@SpringBootTest(classes = TestConfiguration.class)
public class LoginUseCaseFunctionalTest extends AbstractDatabaseContainersTest {
    @Autowired
    LoginUseCase loginUseCase;

    @Autowired
    RegisterUseCase registerUseCase;

    private void createCredentials() {
        String dateString = "2025-05-24";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        RegisterCommand command = new RegisterCommand(
                "test@test.com",
                "MotDePasseTest06?",
                "john",
                "doe",
                "+33650333340",
                LocalDate.parse(dateString, formatter),
                new Role(RoleName.CONSUMER, null));
        this.registerUseCase.register(command);
    }

    @BeforeAll
    void init() {
        this.createCredentials();
    }

    @Test
    void shouldThrowErrorIfCredentialsNotFound() {
        LoginCommand loginCommand = new LoginCommand("not-a-user@email.com", "LePoissonSteve?2");

        assertThrows(UserNotFoundException.class, () -> this.loginUseCase.login(loginCommand));
    }

    @Test
    @Transactional
    void shouldThrowErrorIfPasswordNotMatch() {
        LoginCommand loginCommand = new LoginCommand("test@test.com", "MauvaisMotDePasse33?");

        assertThrows(PasswordNotMatchException.class, () -> this.loginUseCase.login(loginCommand));
    }

    @Test
    @Transactional
    void shouldReturnTokenObject() {
        LoginCommand loginCommand = new LoginCommand("test@test.com", "MotDePasseTest06?");

        Token resultToken = this.loginUseCase.login(loginCommand);

        assertFalse(resultToken.getAccessToken().isBlank());
        assertFalse(resultToken.getRefreshToken().isBlank());
    }
}
