package com.clickandeat.authentication.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
public class ScopeUnitTest {
  @Test
  public void scope_shouldReturnFormattedValue_whenCreatedWithActionAndResource() {
    Scope scope = new Scope("read", "menu");
    assertEquals("read:menu", scope.getScope());
  }
}
