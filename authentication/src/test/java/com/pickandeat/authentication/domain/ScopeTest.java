package com.pickandeat.authentication.domain;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ScopeTest {
    @Test
    public void shouldCreateAScope() {
        Scope scope = new Scope(1, "read", "menu");
        assertEquals("read:menu", scope.getScope());
        assertFalse(scope.isAdmin());
    }

    @Test
    public void shouldCreateAdminScope() {
        Scope scope = new Scope(2, "*", "*");
        assertTrue(scope.isAdmin());
    }
}
