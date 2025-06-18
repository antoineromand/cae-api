package com.pickandeat.authentication.application.usecase.update_password;

import com.pickandeat.authentication.application.exceptions.DatabaseTechnicalException;
import com.pickandeat.authentication.application.exceptions.PasswordNotMatchException;
import com.pickandeat.authentication.application.exceptions.UserNotFoundException;
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
        // find user By userId -> return Domain
        Credentials credentials = this.credentialsRepository.findByUserId(command.userId().toString()).orElseThrow(() -> new UserNotFoundException(command.userId().toString()));
        // match oldPassword with userId password
        if (!this.passwordService.matches(command.oldPassword(),  credentials.getPassword())) {
            throw new PasswordNotMatchException();
        }
        // encrypt newPassword
        String encryptedPassword;
        try {
            encryptedPassword = this.passwordService.hashPassword(command.newPassword());
        } catch (Exception e) {
            throw new CannotHashPasswordException(e);
        }
        // change password -> Domain
        credentials.changePassword(encryptedPassword);
        // call save method from credentials repository
        try {
            this.credentialsRepository.save(credentials);
        } catch (Exception e) {
            throw new DatabaseTechnicalException("Failed to update credentials", e);
        }
        // TODO: send confirmation email
    }
}
