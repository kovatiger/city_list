package com.example.test_task.unit.service.impl;

import com.example.test_task.exception.ResourceNotFoundException;
import com.example.test_task.model.User;
import com.example.test_task.security.JwtTokenProvider;
import com.example.test_task.service.UserService;
import com.example.test_task.service.impl.AuthServiceImpl;
import com.example.test_task.web.dto.JwtRequest;
import com.example.test_task.web.dto.JwtResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private AuthServiceImpl authService;

    private JwtRequest jwtRequest;
    private JwtResponse jwtResponse;
    private User user;
    private String accessToken;
    private String refreshToken;
    private String newRefreshToken;

    @BeforeEach
    void init() {

        jwtRequest = createJwtRequest();

        jwtResponse = createJwtResponse();

        user = createUser();

        accessToken = "access";
        refreshToken = "refresh";
        newRefreshToken = "newRefresh";
    }

    @Test
    void login_everythingOkay_returnJWtResponse() {
        when(userService.getUserByLogin(user.getLogin())).thenReturn(user);
        when(jwtTokenProvider.createAccessToken(user.getId(), user.getLogin(), user.getRoles())).thenReturn(accessToken);
        when(jwtTokenProvider.createRefreshToken(user.getId(), user.getLogin())).thenReturn(refreshToken);

        JwtResponse jwtResponse = authService.login(jwtRequest);

        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(jwtRequest.getLogin(), jwtRequest.getPassword()));
        assertEquals(user.getLogin(), jwtResponse.getLogin());
        assertEquals(user.getId(), jwtResponse.getId());
        assertNotNull(accessToken);
        assertNotNull(refreshToken);
    }

    @Test
    void login_userDoenstExists_throwException() {
        when(userService.getUserByLogin(user.getLogin())).thenThrow(ResourceNotFoundException.class);

        verifyNoInteractions(jwtTokenProvider);

        assertThrows(ResourceNotFoundException.class, () ->
                authService.login(jwtRequest));
    }

    @Test
    void refreshTokens() {
        when(jwtTokenProvider.refreshUserTokens(refreshToken)).thenReturn(jwtResponse);

        JwtResponse actualJwtResponse = authService.refreshTokens(refreshToken);

        assertEquals(jwtResponse, actualJwtResponse);
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

    private User createUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .login("user")
                .password("user")
                .roles(Collections.emptySet())
                .build();
    }
}