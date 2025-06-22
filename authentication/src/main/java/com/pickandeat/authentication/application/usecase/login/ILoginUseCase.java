package com.pickandeat.authentication.application.usecase.login;

import com.pickandeat.authentication.application.TokenPair;

public interface ILoginUseCase {
    TokenPair execute(LoginCommand command);
}
