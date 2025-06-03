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
import com.pickandeat.authentication.application.usecase.IRegisterUseCase;
import com.pickandeat.authentication.application.usecase.RegisterCommand;

import jakarta.validation.Valid;

@RestController()
@RequestMapping("public/api/v1/authentication")
public class PublicAuthenticationController {

    private final IRegisterUseCase registerUseCase;

    public PublicAuthenticationController(IRegisterUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

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
