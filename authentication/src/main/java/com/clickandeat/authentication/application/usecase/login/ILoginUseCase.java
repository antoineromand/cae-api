package com.clickandeat.authentication.application.usecase.login;

import com.clickandeat.authentication.application.TokenPair;
import com.clickandeat.shared.enums.RoleName;

public interface ILoginUseCase {
  TokenPair execute(LoginCommand command, RoleName expectedRole);
}
