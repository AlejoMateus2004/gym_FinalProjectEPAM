package com.gymepam.service.util;

import com.gymepam.domain.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class validatePasswordImplTest {

    @InjectMocks
    private ValidatePasswordImpl validatePassword;

    User user;
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
    void validatePasswordWithValidPassword(){
        String password = "xljLK1Q821";
        assertTrue(validatePassword.validatePassword(user,password));
    }

    @DisplayName("Password matching whit a invalid password")
    @Test
    void validatePasswordWithInvalidPassword() {
        String password = "xlj44332";
        assertFalse(validatePassword.validatePassword(user,password));
    }
}