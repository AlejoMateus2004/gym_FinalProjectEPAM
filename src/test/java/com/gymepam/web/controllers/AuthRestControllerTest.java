package com.gymepam.web.controllers;

import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.Login.AuthenticationResponse;
import com.gymepam.service.facade.LoginFacadeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class AuthRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginFacadeService loginFacadeService;

    @Test
    void login() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("username");
        authenticationRequest.setPassword("password");

        AuthenticationResponse mockedResponse = new AuthenticationResponse();
        mockedResponse.setJwt("token123");

        Mockito.when(loginFacadeService.getAuthenticationResponse(Mockito.any(AuthenticationRequest.class)))
                .thenReturn(ResponseEntity.ok(mockedResponse));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\",\"password\":\"password\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("token123"));
    }

    @Test
    void changeLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"newUsername\",\"oldPassword\":\"oldPassword\",\"newPassword\":\"newPassword\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
