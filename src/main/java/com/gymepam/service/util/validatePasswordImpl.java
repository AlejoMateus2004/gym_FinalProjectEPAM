package com.gymepam.service.util;

import com.gymepam.domain.User;
import org.springframework.stereotype.Service;

@Service
public class validatePasswordImpl implements validatePassword{
    @Override
    public boolean validatePassword(User user, String oldPassword) {
        String userPassword = user.getPassword();
        return userPassword.equals(oldPassword);
    }
}
