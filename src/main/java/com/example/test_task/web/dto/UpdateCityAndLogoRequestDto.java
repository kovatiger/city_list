package com.example.test_task.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(description = "dto for updating city and its logo")
public class UpdateCityAndLogoRequestDto {
    @Schema(name = "currentCityName", example = "Minsk")
    @NotBlank
    private String currentCityName;
    @Schema(name = "newCityName", example = "Mensk")
    @NotBlank
    private String newCityName;
    @Schema(name = "file")
    @NotNull
    private MultipartFile file;
}