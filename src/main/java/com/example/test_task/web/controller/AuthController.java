package com.example.test_task.web.controller;

import com.example.test_task.service.AuthService;
import com.example.test_task.web.dto.JwtRequest;
import com.example.test_task.web.dto.JwtResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Auth Controller", description = "Controller to authorize users")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody JwtRequest jwtRequest) {
        return new ResponseEntity<>(authService.login(jwtRequest), HttpStatus.OK);
    }

    @GetMapping("/refresh")
    @Operation(summary = "Refresh user tokens")
    public ResponseEntity<JwtResponse> refreshTokens(@RequestParam(name = "refreshToken")
                                                     @NotBlank(message = "Token can't be empty") String refreshToken) {
        return new ResponseEntity<>(authService.refreshTokens(refreshToken), HttpStatus.OK);
    }
}