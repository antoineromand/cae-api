package com.clickandeat.authentication.application.usecase.refresh_token;

import com.clickandeat.authentication.application.TokenPair;

public interface IRefreshUseCase {
  TokenPair execute(String refreshToken);
}
