package com.example.test_task.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "Country name dto")
public class CountryResponseDto {
    @Schema(name = "country name", example = "Belarus")
    @NotBlank
    private String countryName;
}