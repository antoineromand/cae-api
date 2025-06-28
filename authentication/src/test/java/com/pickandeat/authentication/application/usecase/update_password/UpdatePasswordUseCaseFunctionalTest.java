package com.pickandeat.authentication.application.usecase.update_password;

import com.pickandeat.authentication.application.exceptions.application.PasswordNotMatchException;
import com.pickandeat.authentication.application.exceptions.application.UserNotFoundException;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.enums.RoleName;
import com.pickandeat.authentication.domain.repository.ICredentialsRepository;
import com.pickandeat.authentication.domain.service.IPasswordService;
import com.pickandeat.authentication.domain.valueobject.Role;
import com.pickandeat.authentication.infrastructure.database.AbstractDatabaseContainersTest;
import com.pickandeat.authentication.infrastructure.repository.CredentialsEntityJPARepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("functional")
class UpdatePasswordUseCaseFunctionalTest extends AbstractDatabaseContainersTest {

  private static final String OLD_PASSWORD = "clearPassword06?";
  private static final String NEW_PASSWORD = "clearPassword07?";

  @Autowired
  private UpdatePasswordUseCase updatePasswordUseCase;
  @Autowired
  private ICredentialsRepository credentialsRepository;
  @Autowired
  private IPasswordService passwordService;
  @Autowired
  private CredentialsEntityJPARepository credentialsEntityJPARepository;

  private UUID existingUserId;

  @BeforeEach
  void setUp() {
    Credentials credentials =
            new Credentials(
                    null,
                    "test-update-password@test.fr",
                    this.passwordService.hashPassword(OLD_PASSWORD),
                    new Role(RoleName.CONSUMER, null),
                    Date.from(Instant.now()),
                    null);

    existingUserId = this.credentialsRepository.save(credentials);
  }

  @Test
  void shouldThrow_whenUserDoesNotExist() {
    UpdatePasswordCommand command =
            new UpdatePasswordCommand(UUID.randomUUID(), "whatever", NEW_PASSWORD);

    assertThrows(UserNotFoundException.class, () -> updatePasswordUseCase.execute(command));
  }

  @Test
  @Transactional
  void shouldThrow_whenOldPasswordDoesNotMatch() {
    UpdatePasswordCommand command =
            new UpdatePasswordCommand(existingUserId, "wrongOldPwd", NEW_PASSWORD);

    assertThrows(PasswordNotMatchException.class, () -> updatePasswordUseCase.execute(command));
  }

  @Test
  @Transactional
  void shouldUpdatePassword_whenEverythingIsValid() {
    UpdatePasswordCommand command =
            new UpdatePasswordCommand(existingUserId, OLD_PASSWORD, NEW_PASSWORD);

    updatePasswordUseCase.execute(command);

    Credentials creds = credentialsRepository.findByUserId(existingUserId.toString()).orElseThrow();

    assertAll(
            () ->
                    assertFalse(
                            passwordService.matches(OLD_PASSWORD, creds.getPassword()),
                            "L’ancien mot de passe ne doit plus matcher"),
            () ->
                    assertTrue(
                            passwordService.matches(NEW_PASSWORD, creds.getPassword()),
                            "Le nouveau mot de passe doit matcher"));
  }

  @AfterEach
  void tearDown() {
    credentialsEntityJPARepository.deleteAll();
  }
}
