package com.pickandeat.authentication.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pickandeat.authentication.infrastructure.model.CredentialsEntity;

@Repository
public interface CredentialsEntityJPARepository extends JpaRepository<CredentialsEntity, UUID> {
    Optional<CredentialsEntity> findByEmail(String email);
}
