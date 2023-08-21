package com.example.test_task.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "City name dto")
public class CityByCountryNameResponseDto {
    @Schema(name = "city name", example = "Minsk")
    @NotBlank
    private String city;
}