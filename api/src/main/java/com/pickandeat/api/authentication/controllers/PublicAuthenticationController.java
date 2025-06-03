package com.pickandeat.api.authentication.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickandeat.api.authentication.dto.RegisterRequestDto;
import com.pickandeat.api.authentication.mapper.RegisterRequestMapper;
import com.pickandeat.authentication.application.usecase.RegisterCommand;

import jakarta.validation.Valid;

@RestController()
@RequestMapping("public/api/v1/authentication")
public class PublicAuthenticationController {
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto dto) throws Exception {
        try {
            RegisterCommand command = RegisterRequestMapper.toCommand(dto);
            return ResponseEntity.ok(command.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/test")
    public String getMethodName() {
        return "haha";
    }

}
