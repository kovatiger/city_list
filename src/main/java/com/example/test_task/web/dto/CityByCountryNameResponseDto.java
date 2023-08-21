package com.example.test_task.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "City name dto")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityByCountryNameResponseDto {
    @Schema(name = "city", example = "Minsk")
    @NotBlank
    private String city;
}