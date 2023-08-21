package com.example.test_task.web.controller;

import com.example.test_task.model.City;
import com.example.test_task.service.CityService;
import com.example.test_task.service.ImageService;
import com.example.test_task.web.dto.CityWithLogoResponseDto;
import com.example.test_task.web.dto.UpdateCityAndLogoRequestDto;
import com.example.test_task.web.facade.UpdateCityWithLogoFacade;
import com.example.test_task.web.mapper.CityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cities")
@RequiredArgsConstructor
@Validated
@Tag(name = "City Controller", description = "Controller to manage cities")
public class CityController {

    private final CityService cityService;
    private final ImageService imageService;
    private final CityMapper cityMapper;
    private final UpdateCityWithLogoFacade facade;

    @GetMapping("/city")
    @Operation(summary = "Get city by particular city name")
    public ResponseEntity<CityWithLogoResponseDto> getCityByCityName(@RequestParam @NotBlank String cityName) {
        City city = cityService.findCityByCityName(cityName);
        String imageUrl = imageService.getImageUrlByLogoName(city.getCountry().getLogoName());
        CityWithLogoResponseDto responseCityDto = new CityWithLogoResponseDto(cityName, imageUrl);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseCityDto);
    }

    @GetMapping()
    @Operation(summary = "Get all cities using pagination")
    public ResponseEntity<List<CityWithLogoResponseDto>> getAllCities(
            @RequestParam(required = false, defaultValue = "0")
            @PositiveOrZero(message = "Page number cant be negative") int pageNumber,
            @RequestParam(required = false, defaultValue = "3")
            @Positive(message = "Page size cant be negative or equal zero") int pageSize) {
        Page<City> cityPage = cityService.findAllCities(pageNumber, pageSize);
        List<CityWithLogoResponseDto> citiesDtoList = imageService.getListOfCitiesWithLogos(cityMapper.getCitiesWithLogoDto(cityPage));
        return new ResponseEntity<>(citiesDtoList, HttpStatus.OK);
    }

    @PatchMapping(value = "/city", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Update city name and country logo")
    @PreAuthorize("@customSecurityExpression.canEdit()")
    public ResponseEntity<Void> updateCityAndLogo(@ModelAttribute UpdateCityAndLogoRequestDto requestDto) {
        facade.updateCityWithLogo(requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}