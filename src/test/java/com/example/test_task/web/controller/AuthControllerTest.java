package com.example.test_task.web.controller;

import com.example.test_task.exception.AccessDeniedException;
import com.example.test_task.exception.ControllerAdvice;
import com.example.test_task.exception.ResourceNotFoundException;
import com.example.test_task.service.AuthService;
import com.example.test_task.web.dto.JwtRequest;
import com.example.test_task.web.dto.JwtResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;
    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private String accessToken;
    private String refreshToken;
    private String newRefreshToken;
    private JwtRequest jwtRequest;
    private JwtResponse jwtResponse;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setControllerAdvice(ControllerAdvice.class)
                .build();

        jwtRequest = createJwtRequest();

        jwtResponse = createJwtResponse();

        accessToken = "access";
        refreshToken = "refresh";
        newRefreshToken = "newRefresh";

        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    void login_everythingOkay_returnJwtResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(jwtRequest);
        when(authService.login(jwtRequest)).thenReturn(jwtResponse);

        ResultActions type = mockMvc.perform(post("/api/v1/auth/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        String actual = type.andReturn().getResponse().getContentAsString();

        assertEquals(jwtResponse, objectMapper.readValue(actual, JwtResponse.class));
    }

    @Test
    void login_userDoesntExists_returnNotFound() throws Exception {
        String requestBody = objectMapper.writeValueAsString(jwtRequest);
        when(authService.login(jwtRequest)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(post("/api/v1/auth/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void refreshTokens_everythingOkay_returnJwtResponse() throws Exception {
        when(authService.refreshTokens(refreshToken)).thenReturn(jwtResponse);

        ResultActions type = mockMvc.perform(get("/api/v1/auth/refresh")
                        .param("refreshToken", "refresh")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        String actual = type.andReturn().getResponse().getContentAsString();

        assertEquals(jwtResponse, objectMapper.readValue(actual, JwtResponse.class));
    }

    @Test
    void refreshTokens_tokenInvalid_returnException() throws Exception {
        when(authService.refreshTokens(refreshToken)).thenThrow(AccessDeniedException.class);

        mockMvc.perform(get("/api/v1/auth/refresh")
                        .param("refreshToken", "refresh")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden());
    }

    private JwtRequest createJwtRequest() {
        return JwtRequest.builder()
                .login("user")
                .password("user")
                .build();
    }

    private JwtResponse createJwtResponse() {
        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}