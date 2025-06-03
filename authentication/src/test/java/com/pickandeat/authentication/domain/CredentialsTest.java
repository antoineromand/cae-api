package com.pickandeat.authentication.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.authentication.domain.valueobject.Scope;

public class CredentialsTest {

    @Test
    public void shouldCreateCredentialsInstanceForAdminUser() {
        Set<Scope> scopes = new HashSet<>();
        scopes.add(new Scope("*", "*"));
        Credentials adminCredentials = new Credentials(
                UUID.randomUUID(),
                "test@test.com",
                "hashedPassword",
                new Role(RoleName.ADMIN, scopes),
                new Date(),
                null);

        assertTrue(adminCredentials.getRole().hasWildcardScope());
        assertTrue(adminCredentials.canAccess("read", "menu"));
        assertTrue(adminCredentials.hasAdminRole());
        assertFalse(adminCredentials.hasConsummerRole());
        assertFalse(adminCredentials.hasProRole());

        adminCredentials.changePassword("newHashedPassword");

        assertEquals("newHashedPassword", adminCredentials.getPassword());
        assertNotNull(adminCredentials.getUpdatedAt());
    }

    @Test
    public void shouldCreateCredentialsForConsummerUser() {
        Set<Scope> scopes = new HashSet<>();
        scopes.add(new Scope("read", "menu"));
        scopes.add(new Scope("create", "order"));
        scopes.add(new Scope("read", "order"));
        scopes.add(new Scope("read", "stores"));

        Credentials consumerCredentials = new Credentials(
                UUID.randomUUID(),
                "test@test.com",
                "hashedPassword",
                new Role(RoleName.CONSUMER, scopes),
                new Date(),
                null);
        assertFalse(consumerCredentials.getRole().hasWildcardScope());
        assertTrue(consumerCredentials.canAccess("read", "menu"));
        assertTrue(consumerCredentials.hasConsummerRole());
        assertFalse(consumerCredentials.hasProRole());
        assertFalse(consumerCredentials.hasAdminRole());

    }

    @Test
    public void shouldCreateCredentialsForProUser() {
        Set<Scope> scopes = new HashSet<>();
        scopes.add(new Scope("read", "menu"));
        scopes.add(new Scope("update", "menu"));
        scopes.add(new Scope("accept", "order"));
        scopes.add(new Scope("denial", "order"));
        scopes.add(new Scope("list", "stores"));

        Credentials proCredentials = new Credentials(
                UUID.randomUUID(),
                "test@test.com",
                "hashedPassword",
                new Role(RoleName.PRO, scopes),
                new Date(),
                null);
        assertFalse(proCredentials.getRole().hasWildcardScope());
        assertFalse(proCredentials.canAccess("create", "order"));
        assertTrue(proCredentials.canAccess("update", "menu"));
        assertTrue(proCredentials.hasProRole());
        assertFalse(proCredentials.hasConsummerRole());
        assertFalse(proCredentials.hasAdminRole());
    }
}
