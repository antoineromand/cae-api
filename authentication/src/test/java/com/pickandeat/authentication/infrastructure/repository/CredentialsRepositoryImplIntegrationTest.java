package com.pickandeat.authentication.infrastructure.repository;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pickandeat.authentication.TestConfiguration;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.authentication.domain.valueobject.Scope;
import com.pickandeat.authentication.infrastructure.database.AbstractDatabaseContainersTest;

import jakarta.transaction.Transactional;

@SpringBootTest(classes = TestConfiguration.class)
public class CredentialsRepositoryImplIntegrationTest extends AbstractDatabaseContainersTest {

    @Autowired
    private CredentialsRepositoryImpl credentialsRepository;

    @Test
    @Transactional
    void shouldSaveAndFindByEmail() {
        String email = "integration@test.com";
        String password = "hashed-password";
        RoleName roleName = RoleName.CONSUMER;

        Role role = new Role(roleName, Set.of(new Scope("read", "menu"))); // scopes inutilisés en save

        Credentials credentials = new Credentials(
                null,
                email,
                password,
                role,
                new Date(),
                null);

        UUID id = credentialsRepository.save(credentials);
        Optional<Credentials> fromDb = credentialsRepository.findByEmail(email);

        assertTrue(fromDb.isPresent());
        assertEquals(email, fromDb.get().getEmail());
        assertEquals(id, fromDb.get().getId());
        assertEquals(roleName, fromDb.get().getRole().name());
    }

    @Test
    @Transactional

    void shouldSaveAndFindById() {
        String email = "integration@test.com";
        String password = "hashed-password";
        RoleName roleName = RoleName.CONSUMER;

        Role role = new Role(roleName, Set.of(new Scope("read", "menu"))); // scopes inutilisés en save

        Credentials credentials = new Credentials(
                null,
                email,
                password,
                role,
                new Date(),
                null);

        UUID id = credentialsRepository.save(credentials);
        Optional<Credentials> fromDb = credentialsRepository.findByUserId(id.toString());

        assertTrue(fromDb.isPresent());
        assertEquals(email, fromDb.get().getEmail());
        assertEquals(id, fromDb.get().getId());
        assertEquals(roleName, fromDb.get().getRole().name());
    }
}
