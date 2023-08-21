package com.example.test_task.web.controller;

import com.example.test_task.exception.ControllerAdvice;
import com.example.test_task.exception.ResourceNotFoundException;
import com.example.test_task.model.City;
import com.example.test_task.model.Country;
import com.example.test_task.service.CityService;
import com.example.test_task.service.ImageService;
import com.example.test_task.web.dto.CityWithLogoResponseDto;
import com.example.test_task.web.mapper.CityMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CityControllerTest {

    @Mock
    private CityService cityService;
    @Mock
    private ImageService imageService;
    @Mock
    private CityMapper cityMapper;
    @InjectMocks
    private CityController cityController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private City city;
    private CityWithLogoResponseDto cityWithLogoResponseDtoWithoutLogo;
    private CityWithLogoResponseDto cityWithLogoResponseDto;
    private String image;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(cityController)
                .setControllerAdvice(ControllerAdvice.class)
                .build();

        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        image = "logo";

        city = createCityWithDefaultProps();
        cityWithLogoResponseDtoWithoutLogo = createCityWithLogoResponseDtoWithoutLogo();
        cityWithLogoResponseDto = createCityWithLogoResponseDto();
    }

    @Test
    void getCityByCityName_everythingOkay_returnResponseDto() throws Exception {
        when(cityService.findCityByCityName(city.getCity())).thenReturn(city);
        when(imageService.getImageByteArrayByLogoName(city.getCountry().getLogoName())).thenReturn(image);

        ResultActions type = mockMvc.perform(get("/api/v1/cities/city")
                        .param("cityName", "Minsk")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        String actual = type.andReturn().getResponse().getContentAsString();

        assertEquals(cityWithLogoResponseDto, objectMapper.readValue(actual, CityWithLogoResponseDto.class));
    }

    @Test
    void getCityByCityName_cityDoesntExist_returnNotFound() throws Exception {
        when(cityService.findCityByCityName(city.getCity())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/api/v1/cities/city")
                        .param("cityName", "Minsk")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(imageService, times(0)).getImageByteArrayByLogoName(city.getCountry().getLogoName());
    }

    @Test
    void getCityByCityName_invalidArguments_throwsException() throws Exception {
        mockMvc.perform(get("/api/v1/cities/city")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        verify(imageService, times(0)).getImageByteArrayByLogoName(city.getCountry().getLogoName());
        verify(cityService, times(0)).findCityByCityName(city.getCity());
    }

    @Test
    void getAllCities_everythingOkay_returnResponseDtosList() throws Exception {
        int pageNumber = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<City> cityPage = new PageImpl<>(List.of(city), pageable, 1);
        when(cityService.findAllCities(pageNumber, pageSize)).thenReturn(cityPage);
        when(cityMapper.getCitiesWithLogoDto(cityPage)).thenReturn(List.of(cityWithLogoResponseDtoWithoutLogo));
        when(imageService.getListOfCitiesWithLogos(List.of(cityWithLogoResponseDtoWithoutLogo)))
                .thenReturn(List.of(cityWithLogoResponseDto));

        mockMvc.perform(get("/api/v1/cities")
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    private City createCityWithDefaultProps() {
        Country country = Country.builder()
                .countryName("Belarus")
                .logoName("by.png")
                .build();
        return City.builder()
                .id(UUID.randomUUID())
                .city("Minsk")
                .country(country)
                .build();
    }

    private CityWithLogoResponseDto createCityWithLogoResponseDtoWithoutLogo() {
        return CityWithLogoResponseDto.builder()
                .city("Minsk")
                .build();
    }

    private CityWithLogoResponseDto createCityWithLogoResponseDto() {
        return CityWithLogoResponseDto.builder()
                .city("Minsk")
                .imageByteArray("logo")
                .build();
    }
}