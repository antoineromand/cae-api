package com.pickandeat.authentication.infrastructure.external.encryption;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pickandeat.authentication.domain.service.IPasswordService;

@Service
public class PasswordService implements IPasswordService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String hashPassword(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean matches(String raw, String hashed) {
        return encoder.matches(raw, hashed);
    }

}
