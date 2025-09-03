package com.clickandeat.authentication.application.usecase.logout;

public interface ILogoutUseCase {
  void execute(String refreshToken);
}
