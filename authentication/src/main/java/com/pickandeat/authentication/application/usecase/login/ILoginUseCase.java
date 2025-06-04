package com.pickandeat.authentication.application.usecase.login;

public interface ILoginUseCase {
    Token login(LoginCommand command);
}
