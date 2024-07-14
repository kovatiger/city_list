package com.example.test_task.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Country name dto")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryResponseDto {
    @Schema(name = "countryName", example = "Belarus")
    @NotBlank
    private String countryName;
}