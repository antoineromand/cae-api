package com.pickandeat.authentication.application.usecase.refresh;

import com.pickandeat.authentication.TestConfiguration;
import com.pickandeat.authentication.application.exceptions.InvalidTokenException;
import com.pickandeat.authentication.application.usecase.login.LoginCommand;
import com.pickandeat.authentication.application.usecase.login.LoginUseCase;
import com.pickandeat.authentication.application.usecase.login.Token;
import com.pickandeat.authentication.application.usecase.register.RegisterCommand;
import com.pickandeat.authentication.application.usecase.register.RegisterUseCase;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.authentication.infrastructure.database.AbstractDatabaseContainersTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest(classes = TestConfiguration.class)
@Transactional
public class RefreshUseCaseFunctionalTest extends AbstractDatabaseContainersTest {
    @Autowired
    LoginUseCase loginUseCase;

    @Autowired
    RegisterUseCase registerUseCase;

    @Autowired
    RefreshUseCase refreshUseCase;

    Token token;

    public Token createCredentials() {
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
        return this.loginUseCase.login(new LoginCommand(command.email(), command.password()));
    }

    @BeforeAll
    void init() {
        this.token = this.createCredentials();
    }

    @Test
    void shouldThrowAnExceptionRefreshToken() {
        Assertions.assertThrows(InvalidTokenException.class, () -> this.refreshUseCase.refreshAccessToken("invalid-token"));
    }

    @Test
    void shouldRefreshToken() {

    }

}
