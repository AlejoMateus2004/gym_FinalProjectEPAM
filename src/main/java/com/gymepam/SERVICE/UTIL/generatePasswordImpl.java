package com.gymepam.SERVICE.UTIL;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
@Service("Gen10Password")
public class generatePasswordImpl implements generatePassword {
    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#&()-_";

    @Override
    public String generatePassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(index);
            password.append(randomChar);
        }
        return password.toString();
    }

}
