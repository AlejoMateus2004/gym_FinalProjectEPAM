package com.gymepam.service.util;

import com.gymepam.domain.entities.User;
import com.gymepam.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class generateUserNameImplTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private GenerateUserNameImpl genUserName;
    @InjectMocks
    private GeneratePasswordImpl genPassword;

    User user;
    List<String> listUserNames;
    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("Alejandro");
        user.setLastName("Mateus");
        user.setUserName("alejandro.mateus");
        user.setPassword(genPassword.generatePassword());
        user.setIsActive(true);

        listUserNames = Collections.singletonList(user.getUserName());
    }

    @Test
    void getUsernameCounts() {
        when(userService.getAllUserNames()).thenReturn(listUserNames);
        assertThat(genUserName.getUsernameCounts().get(listUserNames.get(0))).isEqualTo(1);
    }

    @Test
    void generateUserName() {
        when(userService.getAllUserNames()).thenReturn(listUserNames);
        String userName = genUserName.generateUserName(user.getFirstName(), user.getLastName());
        assertThat(userName).isEqualTo(user.getUserName()+2);
    }


}