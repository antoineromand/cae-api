package com.pickandeat.api.authentication.exception;

import com.pickandeat.api.authentication.controllers.PublicAuthenticationController;
import com.pickandeat.api.shared.GenericApiResponse;
import com.pickandeat.authentication.application.exceptions.application.*;
import com.pickandeat.authentication.application.exceptions.technical.CannotHashPasswordException;
import com.pickandeat.authentication.application.exceptions.technical.DatabaseTechnicalException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackageClasses = { PublicAuthenticationController.class })
public class AuthenticationExceptionHandler {

    private static final String CREDENTIALS_ERROR = "Error while login, please check your credentials.";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(new GenericApiResponse<>("Invalid parameters.", errors));
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<GenericApiResponse<String>> handleErrorInRegisterIfEmailExists(EmailAlreadyUsedException ex) {
        return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(new GenericApiResponse<>(ex.getMessage(), null));
    }

    @ExceptionHandler({DatabaseTechnicalException.class, CannotHashPasswordException.class})
    public ResponseEntity<GenericApiResponse<String>> handleErrorInRegisterIfInsertNotWorking(
            DatabaseTechnicalException ex) {
        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(new GenericApiResponse<>(ex.getMessage(), null));
    }

    @ExceptionHandler({ UserNotFoundException.class, PasswordNotMatchException.class })
    public ResponseEntity<GenericApiResponse<String>> handleLoginError(Exception ex) {
        return ResponseEntity.status(HttpStatusCode.valueOf(401))
                .body(new GenericApiResponse<>(CREDENTIALS_ERROR, null));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<GenericApiResponse<String>> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(new GenericApiResponse<>(ex.getMessage(), null));
    }

    @ExceptionHandler({InvalidTokenException.class, JtiNotFoundInCacheException.class})
    public ResponseEntity<GenericApiResponse<String>> handleInvalidTokenException(Exception ex) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(new GenericApiResponse<>("Error with the refresh token", null));
    }
}
