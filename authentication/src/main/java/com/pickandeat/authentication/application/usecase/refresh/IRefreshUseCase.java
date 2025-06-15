package com.pickandeat.authentication.application.usecase.refresh;

public interface IRefreshUseCase {
    String refreshAccessToken(String refreshToken);
}
