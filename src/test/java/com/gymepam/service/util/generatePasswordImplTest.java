package com.gymepam.service.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
class generatePasswordImplTest {

    @InjectMocks
    private GeneratePasswordImpl genPassword;

    @Test
    void generatePassword() {
        assertNotNull(genPassword.generatePassword());
        assertThat(genPassword.generatePassword().length()).isEqualTo(10);
    }
}