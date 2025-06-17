package com.pickandeat.authentication.application.usecase.refresh;

import com.pickandeat.authentication.application.usecase.login.Token;

public interface IRefreshUseCase {
    Token execute(String refreshToken);
}
