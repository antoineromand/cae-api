package com.pickandeat.authentication.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.pickandeat.authentication.domain.Credentials;

public interface ICredentialsRespository {
    Optional<Credentials> findByEmail(String email);

    UUID save(Credentials credentials);

    Optional<Credentials> findByUserId(String userId);
}
