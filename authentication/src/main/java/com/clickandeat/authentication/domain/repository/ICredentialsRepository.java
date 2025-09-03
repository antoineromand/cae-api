package com.clickandeat.authentication.domain.repository;

import com.clickandeat.authentication.domain.Credentials;
import java.util.Optional;
import java.util.UUID;

public interface ICredentialsRepository {
  Optional<Credentials> findByEmail(String email);

  UUID save(Credentials credentials);

  Optional<Credentials> findByUserId(String userId);
}
