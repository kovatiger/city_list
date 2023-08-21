package com.example.test_task.service.impl;

import com.example.test_task.exception.ResourceNotFoundException;
import com.example.test_task.model.City;
import com.example.test_task.model.Country;
import com.example.test_task.repository.CountryRepository;
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
class CountryServiceImplTest {

    @Mock
    private CountryRepository countryRepository;
    @InjectMocks
    private CountryServiceImpl countryService;

    private String countryName;
    private String newLogoName;
    private Country country;
    private List<Country> countries;
    private List<City> cities;

    @BeforeEach
    void init() {
        countryName = "Belarus";

        newLogoName = "newBy.png";

        countries = List.of(
                Country.builder().id(UUID.randomUUID()).countryName("Belarus").build(),
                Country.builder().id(UUID.randomUUID()).countryName("Ukrain").build(),
                Country.builder().id(UUID.randomUUID()).countryName("UK").build()
        );

        cities = List.of(
                City.builder().id(UUID.randomUUID()).city("Mogilev").build(),
                City.builder().id(UUID.randomUUID()).city("Gomel").build(),
                City.builder().id(UUID.randomUUID()).city("Vitebsk").build()
        );

        country = createCountryWithDefaultProps();
    }

    @Test
    void updateLogoName_everythingOkay_successfullySaved() {
        countryService.updateLogoName(country, newLogoName);

        assertEquals(newLogoName, country.getLogoName());
        verify(countryRepository, times(1)).save(country);
    }

    @Test
    void findAllCountries_returnCountryPage() {
        int pageNumber = 0;
        int pageSize = 3;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Country> expectedPage = new PageImpl<>(countries, pageable, countries.size());
        when(countryRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Country> result = countryService.findAllCountries(pageNumber, pageSize);

        assertEquals(expectedPage, result);
    }

    @Test
    void findAllCountries_NegativePageNumber_ThrowsException() {
        int pageNumber = -1;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(1, 3);

        assertThrows(IllegalArgumentException.class,
                () -> countryService.findAllCountries(pageNumber, pageSize));

        verify(countryRepository, times(0)).findAll(pageable);
    }

    @Test
    void findCitiesByCountryName_countryExists_returnCityList() {
        when(countryRepository.findByCountryName(countryName)).thenReturn(Optional.of(country));

        List<City> actualCities = countryService.findCitiesByCountryName(countryName);

        assertEquals(cities, actualCities);
    }

    @Test
    void findCitiesByCountryName_countryDoesntExist_throwsException() {
        when(countryRepository.findByCountryName(countryName)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> countryService.findCitiesByCountryName(countryName));

        verify(countryRepository, times(1)).findByCountryName(countryName);
    }

    private Country createCountryWithDefaultProps() {
        return Country.builder()
                .id(UUID.randomUUID())
                .countryName("Belarus")
                .cities(cities)
                .build();
    }
}