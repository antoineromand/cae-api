package com.pickandeat.authentication.application.usecase.update_password;

import com.pickandeat.authentication.application.exceptions.application.PasswordNotMatchException;
import com.pickandeat.authentication.application.exceptions.application.UserNotFoundException;
import com.pickandeat.authentication.application.exceptions.technical.CannotHashPasswordException;
import com.pickandeat.authentication.application.exceptions.technical.DatabaseTechnicalException;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.repository.ICredentialsRepository;
import com.pickandeat.authentication.domain.service.IPasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Tag("unit")
public class UpdatePasswordUseCaseUnitTest {

  private UpdatePasswordUseCase updatePasswordUseCase;
  private ICredentialsRepository credentialsRepository;
  private IPasswordService passwordService;

  private final UpdatePasswordCommand command =
          new UpdatePasswordCommand(UUID.randomUUID(), "oldPassword", "newPassword");

  @BeforeEach
  public void setUp() {
    this.credentialsRepository = mock(ICredentialsRepository.class);
    this.passwordService = mock(IPasswordService.class);
    this.updatePasswordUseCase =
            new UpdatePasswordUseCase(this.credentialsRepository, this.passwordService);
  }

  @Test
  public void updatePassword_shouldThrowException_whenUserIdNotFound() {
    when(this.credentialsRepository.findByUserId(command.userId().toString()))
            .thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> this.updatePasswordUseCase.execute(command));
  }

  @Test
  public void updatePassword_shouldThrowException_whenOldPasswordNotMatch() {
    Credentials credentials = mock(Credentials.class);
    when(credentials.getPassword()).thenReturn("notOldPassword");
    when(this.credentialsRepository.findByUserId(command.userId().toString()))
            .thenReturn(Optional.of(credentials));
    when(this.passwordService.matches(command.oldPassword(), credentials.getPassword()))
            .thenReturn(false);
    assertThrows(
            PasswordNotMatchException.class, () -> this.updatePasswordUseCase.execute(command));
  }

  @Test
  public void updatePassword_shouldThrowException_whenPasswordEncryptionFailed() {
    Credentials credentials = mock(Credentials.class);
    RuntimeException rootCause = new RuntimeException("Hashing failed");

    when(credentials.getPassword()).thenReturn("oldPassword");
    when(this.credentialsRepository.findByUserId(command.userId().toString()))
            .thenReturn(Optional.of(credentials));
    when(this.passwordService.matches(command.oldPassword(), credentials.getPassword()))
            .thenReturn(true);

    when(this.passwordService.hashPassword(command.newPassword())).thenThrow(rootCause);
    assertThrows(
            CannotHashPasswordException.class, () -> this.updatePasswordUseCase.execute(command));
  }

  @Test
  public void updatePassword_shouldThrowException_whenUpdateNotWorking() {
    Credentials credentials = mock(Credentials.class);
    RuntimeException rootCause = new RuntimeException("Error while updating password");

    when(credentials.getPassword()).thenReturn("oldPassword");
    when(this.credentialsRepository.findByUserId(command.userId().toString()))
            .thenReturn(Optional.of(credentials));
    when(this.passwordService.matches(command.oldPassword(), credentials.getPassword()))
            .thenReturn(true);

    when(this.passwordService.hashPassword(command.newPassword())).thenReturn("newHashedPassword");
    when(this.credentialsRepository.save(credentials)).thenThrow(rootCause);
    assertThrows(
            DatabaseTechnicalException.class, () -> this.updatePasswordUseCase.execute(command));
  }

  @Test
  public void updatePassword_shouldUpdatePassword_whenEverythingIsValid() {
    Credentials credentials = mock(Credentials.class);
    UUID uuid = UUID.randomUUID();

    when(credentials.getPassword()).thenReturn("oldPassword");
    when(this.credentialsRepository.findByUserId(command.userId().toString()))
            .thenReturn(Optional.of(credentials));
    when(this.passwordService.matches(command.oldPassword(), credentials.getPassword()))
            .thenReturn(true);

    when(this.passwordService.hashPassword(command.newPassword())).thenReturn("newHashedPassword");
    when(this.credentialsRepository.save(credentials)).thenReturn(uuid);

    this.updatePasswordUseCase.execute(command);

    verify(passwordService).hashPassword("newPassword");
    verify(credentials).changePassword("newHashedPassword");
    verify(credentialsRepository).save(credentials);
  }
}
