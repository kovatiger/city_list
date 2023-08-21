package com.example.test_task.service.impl;

import com.example.test_task.model.User;
import com.example.test_task.security.JwtTokenProvider;
import com.example.test_task.service.AuthService;
import com.example.test_task.service.UserService;
import com.example.test_task.web.dto.JwtRequest;
import com.example.test_task.web.dto.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtResponse login(JwtRequest jwtRequest) {
        JwtResponse jwtResponse = JwtResponse.builder().build();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getLogin(), jwtRequest.getPassword()));
        User user = userService.getUserByLogin(jwtRequest.getLogin());
        jwtResponse.setId(user.getId());
        jwtResponse.setLogin(user.getLogin());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getLogin(), user.getRoles()));
        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getLogin()));
        return jwtResponse;
    }

    @Override
    public JwtResponse refreshTokens(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }
}