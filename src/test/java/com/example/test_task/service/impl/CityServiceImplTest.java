package com.example.test_task.service.impl;

import com.example.test_task.exception.ResourceNotFoundException;
import com.example.test_task.model.City;
import com.example.test_task.repository.CityRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {

    @Mock
    private CityRepository cityRepository;
    @InjectMocks
    private CityServiceImpl cityService;

    private String cityName;
    private String newCityName;
    private City city;
    private List<City> cities;

    @BeforeEach
    void init() {

        cityName = "Minsk";

        newCityName = "Mensk";

        cities = List.of(
                City.builder().id(UUID.randomUUID()).city("Mogilev").build(),
                City.builder().id(UUID.randomUUID()).city("Gomel").build(),
                City.builder().id(UUID.randomUUID()).city("Vitebsk").build()
        );

        city = createCityWithDefaultProps();
    }

    @Test
    void findCityByCityName_cityExists_returnCity() {
        when(cityRepository.findByCity(cityName)).thenReturn(Optional.of(city));

        City actualCity = cityService.findCityByCityName(cityName);

        verify(cityRepository, times(1)).findByCity(cityName);
        assertEquals(city, actualCity);
    }

    @Test
    void findCityByCityName_cityDoesntExist_throwsException() {
        when(cityRepository.findByCity(cityName)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cityService.findCityByCityName(cityName));

        verify(cityRepository, times(1)).findByCity(cityName);
    }

    @Test
    void findAllCities_ReturnsCityPage() {
        int pageNumber = 0;
        int pageSize = 3;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<City> expectedPage = new PageImpl<>(cities, pageable, cities.size());

        when(cityRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<City> result = cityService.findAllCities(pageNumber, pageSize);

        assertEquals(expectedPage, result);
    }

    @Test
    void findAllCities_NegativePageNumber_ThrowsException() {
        int pageNumber = -1;
        int pageSize = 10;

        Pageable pageable = PageRequest.of(1, 3);

        assertThrows(IllegalArgumentException.class,
                () -> cityService.findAllCities(pageNumber, pageSize));
        verify(cityRepository, times(0)).findAll(pageable);
    }

    @Test
    void updateCityName_everythingOkay_successfullySaved() {
        cityService.updateCityName(city, newCityName);

        assertEquals(newCityName, city.getCity());
        verify(cityRepository, times(1)).save(city);
    }

    private City createCityWithDefaultProps() {
        return City.builder()
                .id(UUID.randomUUID())
                .city("Minsk")
                .build();
    }
}