package com.pickandeat.authentication.application.usecase;

import java.util.UUID;

public interface IRegisterUseCase {
    UUID register(RegisterCommand command);
}
