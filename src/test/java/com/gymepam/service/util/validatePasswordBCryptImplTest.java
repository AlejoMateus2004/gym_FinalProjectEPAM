package com.gymepam.service.util;

import com.gymepam.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class validatePasswordBCryptImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private validatePasswordBCryptImpl validatePassword;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("Alejandro");
        user.setLastName("Mateus");
        user.setUserName("alejandro.mateus");
        user.setPassword("xljLK1Q821");
        user.setIsActive(true);
    }

    @DisplayName("Password matching whit a valid password")
    @Test
    void testValidatePassword() {
        String password = "xljLK1Q821";
        when(passwordEncoder.matches(user.getPassword(), password)).thenReturn(true);
        assertTrue(validatePassword.validatePassword(user,password));
    }
}