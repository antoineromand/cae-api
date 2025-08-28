package com.pickandeat.authentication.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.shared.enums.RoleName;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.authentication.infrastructure.model.CredentialsEntity;
import com.pickandeat.authentication.infrastructure.model.RoleEntity;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
public class CredentialsRepositoryImplUnitTest {
  private CredentialsEntityJPARepository credentialsJPARepo;
  private RoleEntityJPARepository roleJPARepo;
  private CredentialsRepositoryImpl repository;

  @BeforeEach
  void setUp() {
    credentialsJPARepo = mock(CredentialsEntityJPARepository.class);
    roleJPARepo = mock(RoleEntityJPARepository.class);
    repository = new CredentialsRepositoryImpl(credentialsJPARepo, roleJPARepo);
  }

  @Test
  void shouldReturnCredentialsWhenEmailFound() {
    CredentialsEntity entity = mock(CredentialsEntity.class);
    Credentials domain = mock(Credentials.class);

    when(credentialsJPARepo.findByEmail("test@example.com")).thenReturn(Optional.of(entity));
    when(entity.toDomain()).thenReturn(domain);

    Optional<Credentials> result = repository.findByEmail("test@example.com");

    assertTrue(result.isPresent());
    assertEquals(domain, result.get());
  }

  @Test
  void shouldReturnEmptyWhenEmailNotFound() {
    when(credentialsJPARepo.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

    Optional<Credentials> result = repository.findByEmail("notfound@example.com");

    assertTrue(result.isEmpty());
  }

  @Test
  void shouldSaveCredentialsWhenRoleFound() {
    UUID id = UUID.randomUUID();
    RoleEntity roleEntity = mock(RoleEntity.class);

    Credentials domain =
        new Credentials(
            UUID.randomUUID(),
            "john.doe@example.com",
            "encryptedPass",
            new Role(RoleName.CONSUMER, null),
            new Date(),
            null);

    CredentialsEntity entity = CredentialsEntity.fromDomain(domain, roleEntity);

    CredentialsEntity savedEntity = mock(CredentialsEntity.class);

    when(savedEntity.getId()).thenReturn(id);

    when(roleJPARepo.findByName("CONSUMER")).thenReturn(Optional.of(roleEntity));
    when(credentialsJPARepo.save(any())).thenReturn(savedEntity);
    when(savedEntity.getId()).thenReturn(id);

    UUID result = repository.save(domain);

    assertEquals(id, result);
  }

  @Test
  void shouldThrowWhenRoleNotFound() {
    Credentials domain = mock(Credentials.class);
    when(domain.getRole()).thenReturn(new Role(RoleName.ADMIN, null));
    when(roleJPARepo.findByName("ADMIN")).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> repository.save(domain));
  }

  @Test
  void shouldReturnCredentialsByUserId() {
    UUID userId = UUID.randomUUID();
    CredentialsEntity entity = mock(CredentialsEntity.class);
    Credentials domain = mock(Credentials.class);

    when(credentialsJPARepo.findById(userId)).thenReturn(Optional.of(entity));
    when(entity.toDomain()).thenReturn(domain);

    Optional<Credentials> result = repository.findByUserId(userId.toString());

    assertTrue(result.isPresent());
    assertEquals(domain, result.get());
  }
}
