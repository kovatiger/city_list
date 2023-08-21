package com.example.test_task.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "dto for updating city and its logo")
public class UpdateCityAndLogoRequestDto {
    @Schema(name = "current city name", example = "Minsk")
    @NotBlank
    private String currentCityName;
    @Schema(name = "new city name", example = "Mensk")
    @NotBlank
    private String newCityName;
    @Schema(name = "file with new logo")
    @NotNull
    private MultipartFile file;
}