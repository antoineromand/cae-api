package com.pickandeat.authentication.application.usecase.register;

import java.util.UUID;

public interface IRegisterUseCase {
  UUID execute(RegisterCommand command);
}
