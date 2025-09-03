package com.clickandeat.authentication.infrastructure.repository;

import com.clickandeat.authentication.infrastructure.model.CredentialsEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialsEntityJPARepository extends JpaRepository<CredentialsEntity, UUID> {
  Optional<CredentialsEntity> findByEmail(String email);
}
