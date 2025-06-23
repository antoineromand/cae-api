package com.pickandeat.api.authentication.exception;

import com.pickandeat.api.authentication.controllers.PublicAuthenticationController;
import com.pickandeat.api.shared.ErrorApiResponse;
import com.pickandeat.authentication.application.exceptions.application.*;
import com.pickandeat.authentication.application.exceptions.technical.CannotHashPasswordException;
import com.pickandeat.authentication.application.exceptions.technical.DatabaseTechnicalException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice(basePackageClasses = { PublicAuthenticationController.class })
public class AuthenticationExceptionHandler {

    private static final String CREDENTIALS_ERROR = "Error while login, please check your credentials.";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            if (error.getDefaultMessage() != null) {
                errors.computeIfAbsent(error.getField(), k -> new ArrayList<>()).add(error.getDefaultMessage());
            }
        });

        return ResponseEntity.badRequest().body(
                new ErrorApiResponse("INVALID_PARAMETERS", "Some parameters are invalid.", 400, errors)
        );
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ErrorApiResponse> handleErrorInRegisterIfEmailExists(EmailAlreadyUsedException ex) {
        return ResponseEntity.status(HttpStatusCode.valueOf(409)).body(new ErrorApiResponse(ex.getKey(), ex.getMessage(), 409, null));
    }

    @ExceptionHandler({DatabaseTechnicalException.class, CannotHashPasswordException.class})
    public ResponseEntity<ErrorApiResponse> handleErrorInRegisterIfInsertNotWorking() {
        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(new ErrorApiResponse("INTERNAL_SERVER_ERROR", "The server encountered an internal error.", 500, null));
    }

    @ExceptionHandler({ UserNotFoundException.class, PasswordNotMatchException.class })
    public ResponseEntity<ErrorApiResponse> handleLoginError() {
        return ResponseEntity.status(HttpStatusCode.valueOf(401))
                .body(new ErrorApiResponse("INVALID_CREDENTIALS", CREDENTIALS_ERROR, 401, null));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorApiResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(new ErrorApiResponse("MISSING_HEADER", ex.getMessage(), 400, null));
    }

    @ExceptionHandler({InvalidTokenException.class, JtiNotFoundInCacheException.class})
    public ResponseEntity<ErrorApiResponse> handleInvalidTokenException(AbstractApplicationException ex) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(new ErrorApiResponse(ex.getKey(), ex.getMessage(), 400, null));
    }
}
