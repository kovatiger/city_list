package com.example.test_task.service;

import com.example.test_task.web.dto.JwtRequest;
import com.example.test_task.web.dto.JwtResponse;

/**
 * Service for login user
 */
public interface AuthService {

    /**
     * Method for user login
     *
     * @param jwtRequest jwt request consists of login and password
     * @return {@link JwtResponse} jwt response consists of id, login, access and refresh tokens
     */
    JwtResponse login(JwtRequest jwtRequest);

    /**
     * Method for getting new tokens by refresh token
     *
     * @param refreshToken refresh token for getting a new pair of tokens
     * @return {@link JwtResponse} jwt response consists of id, login, access and refresh tokens
     */
    JwtResponse refreshTokens(String refreshToken);
}