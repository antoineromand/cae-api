package com.pickandeat.authentication.application.usecase.update_password;

import com.pickandeat.authentication.application.exceptions.application.PasswordNotMatchException;
import com.pickandeat.authentication.application.exceptions.application.UserNotFoundException;
import com.pickandeat.authentication.application.exceptions.technical.DatabaseTechnicalException;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.exceptions.CannotHashPasswordException;
import com.pickandeat.authentication.domain.repository.ICredentialsRepository;
import com.pickandeat.authentication.domain.service.IPasswordService;
import org.springframework.stereotype.Service;


@Service
public class UpdatePasswordUseCase implements IUpdatePasswordUseCase {

    private final ICredentialsRepository credentialsRepository;
    private final IPasswordService passwordService;

    public UpdatePasswordUseCase(ICredentialsRepository credentialsRepository, IPasswordService passwordService) {
        this.credentialsRepository = credentialsRepository;
        this.passwordService = passwordService;
    }

    public void execute(UpdatePasswordCommand command) {
        Credentials credentials = this.findCredentials(command.userId().toString());
        this.checkOldPassword(command.oldPassword(), credentials.getPassword());
        String encryptedPassword = this.hashPassword(command.newPassword());
        credentials.changePassword(encryptedPassword);
        this.updateCredentials(credentials);
        // TODO: send confirmation email
    }

    private Credentials findCredentials(String userId) {
        return this.credentialsRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
    }

    private void checkOldPassword(String oldPassword, String currentHashedPassword) {
        if (!this.passwordService.matches(oldPassword,  currentHashedPassword)) {
            throw new PasswordNotMatchException();
        }
    }

    private String hashPassword(String password) {
        try {
            return this.passwordService.hashPassword(password);
        } catch (Exception e) {
            throw new CannotHashPasswordException(e);
        }
    }

    private void updateCredentials(Credentials credentials) {
        try {
            this.credentialsRepository.save(credentials);
        } catch (Exception e) {
            throw new DatabaseTechnicalException("Failed to update credentials", e);
        }
    }
}
