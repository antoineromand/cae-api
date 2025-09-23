package com.clickandeat.authentication.application.usecase.register;

import com.clickandeat.authentication.application.exceptions.application.EmailAlreadyUsedException;
import com.clickandeat.authentication.application.exceptions.technical.CannotHashPasswordException;
import com.clickandeat.authentication.application.exceptions.technical.DatabaseTechnicalException;
import com.clickandeat.authentication.domain.Credentials;
import com.clickandeat.authentication.domain.repository.ICredentialsRepository;
import com.clickandeat.authentication.domain.service.IPasswordService;
import java.util.Date;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegisterUseCase implements IRegisterUseCase {

  private final ICredentialsRepository credentialsRepository;
  private final IPasswordService passwordService;

  public RegisterUseCase(ICredentialsRepository respository, IPasswordService service) {
    this.credentialsRepository = respository;
    this.passwordService = service;
  }

  @Override
  public UUID execute(RegisterCommand command) {
    ensureEmailIsUnique(command.email());

    String hashedPassword = hashPassword(command.password());
    Credentials credentials = createCredentials(command, hashedPassword);

    return persistCredentials(credentials);
  }

  private void ensureEmailIsUnique(String email) {
    if (credentialsRepository.findByEmail(email).isPresent()) {
      throw new EmailAlreadyUsedException();
    }
  }

  private String hashPassword(String plainPassword) {
    try {
      return passwordService.hashPassword(plainPassword);
    } catch (Exception e) {
      throw new CannotHashPasswordException(e);
    }
  }

  private Credentials createCredentials(RegisterCommand command, String hashedPassword) {
    return new Credentials(null, command.email(), hashedPassword, command.role(), new Date(), null);
  }

  private UUID persistCredentials(Credentials credentials) {
    try {
      return credentialsRepository.save(credentials);
    } catch (DataIntegrityViolationException e) {
      throw new DatabaseTechnicalException("An error occurred while inserting the user", e);
    }
  }
}
