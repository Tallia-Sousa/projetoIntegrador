package com.example.code.controllers;

import com.example.code.model.user.AutenticarDTO;
import com.example.code.model.user.User;
import com.example.code.repositories.UserRepository;
import com.example.code.services.AuthorizationService;
import com.example.code.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UsersController.class})
@ExtendWith(SpringExtension.class)
class UsersControllerTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private AuthorizationService authorizationService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UsersController usersController;

    @Test
    void testlogin() throws Exception {
        when(tokenService.generateToken(Mockito.<User>any())).thenReturn("ABC123");
        when(authenticationManager.authenticate(Mockito.<Authentication>any())).thenReturn(new TestingAuthenticationToken(new User(), "Credentials"));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/users/login")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.contentType(objectMapper
                .writeValueAsString(new AutenticarDTO("katymarq@gmail.com", "senha1")));
        MockMvcBuilders.standaloneSetup(usersController).build().perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"token\":\"ABC123\"}"));

    }
    @Test
    void testlogin2() throws Exception{
        when(tokenService.generateToken(Mockito.<User>any())).thenReturn("ABC123");
        when(authenticationManager.authenticate(Mockito.<Authentication>any())).thenReturn(new TestingAuthenticationToken(new User(), "Credentials"));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/users/login")
                .contentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new AutenticarDTO("i.i.i", "Senha")));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(usersController)
                .build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"email\":\"insira um email vÃ¡lido\"}"));

    }
}