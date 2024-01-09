package com.gymepam.service.util;

import com.gymepam.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class validatePasswordBCryptImpl implements validatePassword{

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public boolean validatePassword(User user, String oldPassword) {
        String userPassword = user.getPassword();
        return (passwordEncoder.matches(oldPassword, userPassword));
    }
}
