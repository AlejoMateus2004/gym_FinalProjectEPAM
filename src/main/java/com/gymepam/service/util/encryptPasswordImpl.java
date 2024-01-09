package com.gymepam.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class encryptPasswordImpl implements encryptPassword {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
