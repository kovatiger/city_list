package com.example.test_task.web.dto;

import com.example.test_task.model.Country;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema()
public class CitiesWithLogoResponseDto {
    private String city;
    @JsonIgnore
    private Country country;
    private String logoUrl;
}