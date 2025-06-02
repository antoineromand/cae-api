package com.pickandeat.api.authentication.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api/v1/authentication")
public class PublicAuthenticationController {
    @GetMapping("login")
    public String getMethodName() {
        return "test";
    }

}
