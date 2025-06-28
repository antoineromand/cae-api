package com.pickandeat.authentication.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.authentication.domain.valueobject.Scope;
import com.pickandeat.authentication.infrastructure.database.AbstractDatabaseContainersTest;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Tag("integration")
public class CredentialsRepositoryImplIntegrationTest extends AbstractDatabaseContainersTest {

  @Autowired
  private CredentialsRepositoryImpl credentialsRepository;

  @Test
  @Transactional
  void save_shouldPersistAndFindByEmail_whenEmailExistsInDatabase() {
    String email = "integration@test.com";
    String password = "hashed-password";
    RoleName roleName = RoleName.CONSUMER;

    Role role = new Role(roleName, Set.of(new Scope("read", "menu"))); // scopes inutilisés en save

    Credentials credentials = new Credentials(null, email, password, role, new Date(), null);

    UUID id = credentialsRepository.save(credentials);
    Optional<Credentials> fromDb = credentialsRepository.findByEmail(email);

    assertTrue(fromDb.isPresent());
    assertEquals(email, fromDb.get().getEmail());
    assertEquals(id, fromDb.get().getId());
    assertEquals(roleName, fromDb.get().getRole().name());
  }

  @Test
  @Transactional
  void save_shouldPersistAndFindById_whenIdExistsInDatabase() {
    String email = "integration@test.com";
    String password = "hashed-password";
    RoleName roleName = RoleName.CONSUMER;

    Role role = new Role(roleName, Set.of(new Scope("read", "menu"))); // scopes inutilisés en save

    Credentials credentials = new Credentials(null, email, password, role, new Date(), null);

    UUID id = credentialsRepository.save(credentials);
    Optional<Credentials> fromDb = credentialsRepository.findByUserId(id.toString());

    assertTrue(fromDb.isPresent());
    assertEquals(email, fromDb.get().getEmail());
    assertEquals(id, fromDb.get().getId());
    assertEquals(roleName, fromDb.get().getRole().name());
  }
}
