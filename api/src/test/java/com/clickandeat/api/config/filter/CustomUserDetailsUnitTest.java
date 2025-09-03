package com.clickandeat.api.config.filter;

import static org.junit.jupiter.api.Assertions.*;

import com.clickandeat.authentication.domain.valueobject.Scope;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
public class CustomUserDetailsUnitTest {
  @Test
  void shouldReturnCorrectAuthorities() {
    UUID userId = UUID.randomUUID();
    String role = "ADMIN";
    Set<Scope> scopes = Set.of(new Scope("read", "product"), new Scope("write", "order"));

    CustomUserDetails userDetails = new CustomUserDetails(userId, role, scopes);

    var authorities = userDetails.getAuthorities();

    assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("SCOPE_READ:PRODUCT")));
    assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("SCOPE_WRITE:ORDER")));
    assertEquals(3, authorities.size());
  }

  @Test
  void shouldReturnCorrectUserDetailsData() {
    UUID userId = UUID.randomUUID();
    String role = "USER";
    Set<Scope> scopes = Set.of();

    CustomUserDetails userDetails = new CustomUserDetails(userId, role, scopes);

    assertEquals(userId.toString(), userDetails.getUsername());
    assertNull(userDetails.getPassword());
    assertEquals(userId, userDetails.getUserId());
    assertEquals(role, userDetails.getRole());
    assertEquals(scopes, userDetails.getScopes());

    assertTrue(userDetails.isAccountNonExpired());
    assertTrue(userDetails.isAccountNonLocked());
    assertTrue(userDetails.isCredentialsNonExpired());
    assertTrue(userDetails.isEnabled());
  }
}
