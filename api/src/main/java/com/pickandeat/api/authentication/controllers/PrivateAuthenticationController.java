package com.pickandeat.api.authentication.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("private/api/v1/authentication")
public class PrivateAuthenticationController {
    @PreAuthorize("hasRole('CONSUMER') and hasAuthority('SCOPE_UPDATE:USER-PASSWORD')")
    @GetMapping("test")
    public String test() {
        return "Test";
    }

}
