package com.pickandeat.api.authentication.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickandeat.api.authentication.dto.RegisterRequestDto;
import com.pickandeat.api.authentication.mapper.RegisterRequestMapper;
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

    public PublicAuthenticationController(IRegisterUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    @Operation(summary = "Register a user", description = "Registers a new user and returns their unique identifier.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User registration data", required = true, content = @Content(schema = @Schema(implementation = RegisterRequestDto.class), examples = @ExampleObject(name = "RegisterRequestExample", summary = "Example registration", value = """
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
            @ApiResponse(responseCode = "200", description = "User successfully registered", content = @Content(mediaType = "application/json", schema = @Schema(type = "string", format = "uuid", example = "550e8400-e29b-41d4-a716-446655440000"))),
            @ApiResponse(responseCode = "400", description = "Invalid request body."),
            @ApiResponse(responseCode = "403", description = "Email is already in use."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto dto) throws Exception {
        try {
            RegisterCommand command = RegisterRequestMapper.toCommand(dto);
            UUID userId = this.registerUseCase.register(command);
            return ResponseEntity.ok(userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/test")
    public String getMethodName() {
        return "haha";
    }

}
