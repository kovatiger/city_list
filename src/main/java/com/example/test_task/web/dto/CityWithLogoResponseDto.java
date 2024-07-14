package com.example.test_task.web.dto;

import com.example.test_task.model.Country;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "City with logo url dto")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityWithLogoResponseDto {
    @Schema(name = "city", example = "Minsk")
    @NotBlank
    private String city;
    @JsonIgnore
    private Country country;
    @Schema(name = "imageByteArray")
    private String imageByteArray;

    public CityWithLogoResponseDto(String city, String imageByteArray) {
        this.city = city;
        this.imageByteArray = imageByteArray;
    }
}