package com.pickandeat.authentication.infrastructure.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.authentication.domain.valueobject.Scope;
import com.pickandeat.shared.enums.RoleName;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
class CredentialsEntityUnitTest {

  @Test
  void shouldConvertFromDomainToEntityAndBack() {
    UUID id = UUID.randomUUID();
    String email = "john@example.com";
    String password = "hashedPassword";
    Date createdAt = new Date();
    Date updatedAt = new Date();
    Set<Scope> scopes = Set.of(new Scope("READ", "PRODUCT"));

    Role role = new Role(RoleName.CONSUMER, scopes);
    Credentials domain = new Credentials(id, email, password, role, createdAt, updatedAt);
    RoleEntity roleEntity = mock(RoleEntity.class);
    when(roleEntity.getId()).thenReturn(2);
    when(roleEntity.getName()).thenReturn("CONSUMER");
    CredentialsEntity entity = CredentialsEntity.fromDomain(domain, roleEntity);
    Credentials result = entity.toDomain();

    assertEquals(domain.getId(), result.getId());
    assertEquals(domain.getEmail(), result.getEmail());
    assertEquals(domain.getPassword(), result.getPassword());

    assertEquals(RoleName.CONSUMER, result.getRole().name());
  }

  @Test
  void shouldHandleNullUpdatedAtCorrectly() {
    UUID id = UUID.randomUUID();
    String email = "jane@example.com";
    String password = "securePass";
    Date createdAt = new Date();

    Role role = new Role(RoleName.ADMIN, Set.of());
    Credentials domain = new Credentials(id, email, password, role, createdAt, null);

    RoleEntity roleEntity = mock(RoleEntity.class);
    when(roleEntity.getId()).thenReturn(1);
    when(roleEntity.getName()).thenReturn("ADMIN");

    CredentialsEntity entity = CredentialsEntity.fromDomain(domain, roleEntity);

    assertNotNull(entity);
    assertNull(entity.getUpdatedAt());

    Credentials result = entity.toDomain();
    assertNull(result.getUpdatedAt());
  }
}
