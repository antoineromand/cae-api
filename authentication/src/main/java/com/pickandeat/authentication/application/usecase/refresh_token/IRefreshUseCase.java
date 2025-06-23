package com.pickandeat.authentication.application.usecase.refresh_token;

import com.pickandeat.authentication.application.TokenPair;

public interface IRefreshUseCase {
    TokenPair execute(String refreshToken);
}
