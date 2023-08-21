package com.example.test_task.web.controller;

import com.example.test_task.exception.ControllerAdvice;
import com.example.test_task.exception.ResourceNotFoundException;
import com.example.test_task.model.City;
import com.example.test_task.model.Country;
import com.example.test_task.service.CountryService;
import com.example.test_task.web.dto.CityByCountryNameResponseDto;
import com.example.test_task.web.dto.CountryResponseDto;
import com.example.test_task.web.mapper.CityMapper;
import com.example.test_task.web.mapper.CountryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CountryControllerTest {

    @Mock
    private CountryService countryService;
    @Mock
    private CountryMapper countryMapper;
    @Mock
    private CityMapper cityMapper;
    @InjectMocks
    private CountryController countryController;

    private MockMvc mockMvc;
    private Country country;
    private CountryResponseDto countryResponseDto;
    private List<City> cities;
    private List<CityByCountryNameResponseDto> cityByCountryNameResponseDtos;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(countryController)
                .setControllerAdvice(ControllerAdvice.class)
                .build();

        country = createCountryWithDefaultProps();

        countryResponseDto = createCountryResponseDto();

        cities = List.of(
                City.builder().id(UUID.randomUUID()).city("Mogilev").build(),
                City.builder().id(UUID.randomUUID()).city("Gomel").build(),
                City.builder().id(UUID.randomUUID()).city("Vitebsk").build()
        );

        cityByCountryNameResponseDtos = List.of(
                CityByCountryNameResponseDto.builder().city("Mogilev").build(),
                CityByCountryNameResponseDto.builder().city("Gomel").build(),
                CityByCountryNameResponseDto.builder().city("Vitebsk").build()
        );
    }

    @Test
    void getAllCountries() throws Exception {
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Country> countryPage = new PageImpl<>(List.of(country), pageable, 1);
        when(countryService.findAllCountries(pageNumber, pageSize)).thenReturn(countryPage);
        when(countryMapper.getCountryDto(countryPage)).thenReturn(List.of(countryResponseDto));

        mockMvc.perform(get("/api/v1/countries")
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void getCitiesByCountry() throws Exception {
        when(countryService.findCitiesByCountryName(country.getCountryName())).thenReturn(cities);
        when(cityMapper.getCitiesDto(cities)).thenReturn(cityByCountryNameResponseDtos);

        mockMvc.perform(get("/api/v1/countries/{country}/cities", "Belarus")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void getCitiesByCountry2() throws Exception {
        when(countryService.findCitiesByCountryName(country.getCountryName())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/api/v1/countries/{country}/cities", "Belarus")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(cityMapper, times(0)).getCitiesDto(cities);
    }

    private Country createCountryWithDefaultProps() {
        return Country.builder()
                .id(UUID.randomUUID())
                .countryName("Belarus")
                .build();
    }

    private CountryResponseDto createCountryResponseDto() {
        return CountryResponseDto.builder()
                .countryName("Belarus")
                .build();

    }
}