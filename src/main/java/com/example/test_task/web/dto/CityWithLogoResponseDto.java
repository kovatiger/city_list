package com.example.test_task.web.dto;

import com.example.test_task.model.Country;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "City with logo url dto")
public class CityWithLogoResponseDto {

    @Schema(name = "city name", example = "Minsk")
    @NotBlank
    private String city;
    @JsonIgnore
    private Country country;
    @Schema(name = "logo url")
    private String logoUrl;

    public CityWithLogoResponseDto(String city,String logoUrl) {
        this.city = city;
        this.logoUrl = logoUrl;
    }
}