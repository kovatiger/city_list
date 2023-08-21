package com.example.test_task.web.controller;

import com.example.test_task.model.City;
import com.example.test_task.model.Country;
import com.example.test_task.service.CountryService;
import com.example.test_task.web.dto.CityByCountryNameResponseDto;
import com.example.test_task.web.mapper.CityMapper;
import com.example.test_task.web.mapper.CountryMapper;
import com.example.test_task.web.dto.CountryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
@Validated
@Tag(name = "Country Controller", description = "Controller to manage countries")
public class CountryController {

    private final CountryService countryService;
    private final CountryMapper countryMapper;
    private final CityMapper cityMapper;

    @GetMapping()
    @Operation(summary = "Get all countries using pagination")
    public ResponseEntity<List<CountryResponseDto>> getAllCountries(
            @RequestParam(required = false, defaultValue = "0")
            @PositiveOrZero(message = "Page number cant be negative") int pageNumber,
            @RequestParam(required = false, defaultValue = "3")
            @Positive(message = "Page size cant be negative or equal zero") int pageSize) {
        Page<Country> countries = countryService.findAllCountries(pageNumber, pageSize);
        return new ResponseEntity<>(countryMapper.getCountryDto(countries), HttpStatus.OK);
    }

    @GetMapping("/{country}/cities")
    @Operation(summary = "Get all cities be country")
    public ResponseEntity<List<CityByCountryNameResponseDto>> getCitiesByCountry(@PathVariable
                                                                                 @NotBlank String country) {
        List<City> cities = countryService.findCitiesByCountryName(country);
        return new ResponseEntity<>(cityMapper.getCitiesDto(cities), HttpStatus.OK);
    }
}