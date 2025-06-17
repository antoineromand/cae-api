package com.pickandeat.api.authentication.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickandeat.api.authentication.dto.LoginRequestDto;
import com.pickandeat.api.authentication.dto.RegisterRequestDto;
import com.pickandeat.api.authentication.mapper.LoginRequestMapper;
import com.pickandeat.api.authentication.mapper.RegisterRequestMapper;
import com.pickandeat.api.authentication.swagger.ErrorResponse;
import com.pickandeat.api.authentication.swagger.LoginApiResponse;
import com.pickandeat.api.authentication.swagger.RegisterApiResponse;
import com.pickandeat.api.shared.GenericApiResponse;
import com.pickandeat.authentication.application.usecase.login.ILoginUseCase;
import com.pickandeat.authentication.application.usecase.login.LoginCommand;
import com.pickandeat.authentication.application.usecase.login.Token;
import com.pickandeat.authentication.application.usecase.register.IRegisterUseCase;
import com.pickandeat.authentication.application.usecase.register.RegisterCommand;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController()
@RequestMapping("public/api/v1/authentication")
@Tag(name = "Authentication (Public)", description = "Public endpoints for user registration and login.")
public class PublicAuthenticationController {

    private final IRegisterUseCase registerUseCase;
    private final ILoginUseCase loginUseCase;

    public PublicAuthenticationController(IRegisterUseCase registerUseCase, ILoginUseCase loginUseCase) {
        this.registerUseCase = registerUseCase;
        this.loginUseCase = loginUseCase;
    }

    @Operation(summary = "Register a user", description = "Registers a new user and returns an api response with userID.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User registration data", required = true, content = @Content(schema = @Schema(implementation = RegisterRequestDto.class), examples = @ExampleObject(name = "RegisterRequestExample", summary = "Example registration", value = """
                {
                    "email": "example@example.com",
                    "password": "AstrongPassw0rd!",
                    "firstName": "John",
                    "lastName": "Doe",
                    "phoneNumber": "+33601020304",
                    "birthDate": "1995-01-01",
                    "role": "CONSUMER"
                }
            """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email is already in use.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<GenericApiResponse<UUID>> register(@Valid @RequestBody RegisterRequestDto dto) {
        RegisterCommand command = RegisterRequestMapper.toCommand(dto);
        UUID userId = this.registerUseCase.execute(command);
        return ResponseEntity.status(201).body(new GenericApiResponse<>("Registration completed successfully." + //
                "", userId));
    }

    @Operation(summary = "Log a user", description = "Log a new user and returns an api response with tokens.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User registration data", required = true, content = @Content(schema = @Schema(implementation = RegisterRequestDto.class), examples = @ExampleObject(name = "RegisterRequestExample", summary = "Example registration", value = """
                {
                    "email": "example@example.com",
                    "password": "AstrongPassw0rd!",
                }
            """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully authenticated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Wrong credentials.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<GenericApiResponse<Token>> login(@Valid @RequestBody LoginRequestDto dto) throws Exception {
        LoginCommand command = LoginRequestMapper.toCommand(dto);
        Token token = this.loginUseCase.execute(command);
        return ResponseEntity.ok(new GenericApiResponse<>("Authentication successful.", token));
    }

}
