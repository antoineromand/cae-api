package com.pickandeat.authentication.application.usecase;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickandeat.authentication.application.exceptions.EmailAlreadyUsedException;
import com.pickandeat.authentication.application.exceptions.RegistrationTechnicalException;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.exceptions.CannotHashPasswordException;
import com.pickandeat.authentication.domain.repository.ICredentialsRespository;
import com.pickandeat.authentication.domain.service.IPasswordService;

@Service
@Transactional
public class RegisterUseCase {
    private final ICredentialsRespository credentialsRepository;
    private final IPasswordService passwordService;

    public RegisterUseCase(ICredentialsRespository respository, IPasswordService service) {
        credentialsRepository = respository;
        passwordService = service;
    }

    public UUID register(RegisterCommand command) {

        Optional<Credentials> credentials = credentialsRepository.findByEmail(command.email());

        if (credentials.isPresent()) {
            throw new EmailAlreadyUsedException(command.email());
        }

        String hashedPassword;
        try {
            hashedPassword = this.passwordService.hashPassword(command.password());
        } catch (Exception e) {
            throw new CannotHashPasswordException(e);
        }

        Credentials _credentials = new Credentials(null, command.email(), hashedPassword, command.role(),
                new Date(), null);

        UUID credentialsId;
        try {
            credentialsId = credentialsRepository.save(_credentials);
        } catch (DataIntegrityViolationException e) {
            throw new RegistrationTechnicalException("Une erreur est survenue lors de l'inscription.", e);
        }

        // NEXT TIME : SEND OTHER FIELDS FROM REGISTER COMMAND THROUGHT SPRING EVENTS TO
        // PROFILE MODULE

        // NEXT TIME : GENERATE TOKEN WITH USER ID AND SENT IT TO NOTIFICATION MODULE
        // (EMAIL)

        return credentialsId;
    }
}
