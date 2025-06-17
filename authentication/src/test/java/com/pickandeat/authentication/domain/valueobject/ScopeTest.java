package com.pickandeat.authentication.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ScopeTest {
    @Test
    public void scope_shouldReturnFormattedValue_whenCreatedWithActionAndResource() {
        Scope scope = new Scope("read", "menu");
        assertEquals("read:menu", scope.getScope());
    }

}
