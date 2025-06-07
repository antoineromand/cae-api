package com.pickandeat.authentication.application.usecase.login;

import java.time.Duration;

import org.springframework.stereotype.Service;

import com.pickandeat.authentication.application.exceptions.PasswordNotMatchException;
import com.pickandeat.authentication.application.exceptions.UserNotFoundException;
import com.pickandeat.authentication.domain.Credentials;
import com.pickandeat.authentication.domain.repository.ICredentialsRespository;
import com.pickandeat.authentication.domain.repository.ITokenRepository;
import com.pickandeat.authentication.domain.service.IPasswordService;
import com.pickandeat.shared.token.application.TokenService;

@Service
public class LoginUseCase implements ILoginUseCase {
    private final IPasswordService passwordService;
    private final ICredentialsRespository credentialsRespository;
    private final TokenService tokenService;
    private final ITokenRepository tokenRepository;

    public LoginUseCase(IPasswordService passwordService, ICredentialsRespository repository,
            TokenService tokenService, ITokenRepository tokenRepository) {
        this.passwordService = passwordService;
        this.credentialsRespository = repository;
        this.tokenService = tokenService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Token login(LoginCommand command) {
        // findByEmail
        Credentials credentials = this.credentialsRespository.findByEmail(command.email())
                .orElseThrow(() -> new UserNotFoundException(command.email()));
        // match password
        if (!this.passwordService.matches(command.password(), credentials.getPassword())) {
            throw new PasswordNotMatchException();
        }
        // generate token
        String accessToken = this.tokenService.createAccessToken(credentials.getId(),
                credentials.getRole().name().toString());
        String refreshToken = this.tokenService.createRefreshToken(credentials.getId(),
                credentials.getRole().name().toString());
        Token resultToken = new Token(accessToken, refreshToken);
        // generate & store refresh token (use refresh time in application.properties)
        this.tokenRepository.storeRefreshToken(this.tokenService.extractJti(refreshToken),
                credentials.getId().toString(), Duration.ofMillis(1209600000));
        // return Token
        return resultToken;
    }

}
