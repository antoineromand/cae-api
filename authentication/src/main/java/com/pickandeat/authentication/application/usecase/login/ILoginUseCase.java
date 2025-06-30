package com.pickandeat.authentication.application.usecase.login;

import com.pickandeat.authentication.application.TokenPair;
import com.pickandeat.authentication.domain.enums.RoleName;

public interface ILoginUseCase {
  TokenPair execute(LoginCommand command, RoleName expectedRole);
}
