package com.example.test_task.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Schema(description = "Jwt response")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    @Schema(name = "id", example = "ddb1af2e-0d6e-4ebd-9cf4-c725cae97a07")
    private UUID id;
    @Schema(name = "login", example = "user")
    @NotBlank
    private String login;
    @Schema(name = "accessToken")
    @NotBlank
    private String accessToken;
    @Schema(name = "refreshToken")
    @NotBlank
    private String refreshToken;
}