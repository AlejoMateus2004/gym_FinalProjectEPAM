package com.gymepam.SERVICE.UTIL;

import com.gymepam.DOMAIN.User;
import com.gymepam.SERVICE.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class generateUserNameImplTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private generateUserNameImpl genUserName;
    @InjectMocks
    private generatePasswordImpl genPassword;

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

        listUserNames = Arrays.asList(user.getUserName());
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

    @Test
    void isValidUsername() {
        when(userService.getAllUserNames()).thenReturn(listUserNames);
        String username =user.getUserName();
        assertThat(genUserName.isValidUsername(username,user.getFirstName(),user.getLastName())).isEqualTo(false);
        username =user.getUserName()+"2";
        assertThat(genUserName.isValidUsername(username,user.getFirstName(),user.getLastName())).isEqualTo(true);

    }
}