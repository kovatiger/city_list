package com.example.test_task.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Jwt request")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest {
    @NotBlank
    @Schema(name = "login", example = "User")
    private String login;
    @NotBlank
    @Schema(name = "password", example = "12345")
    private String password;
}