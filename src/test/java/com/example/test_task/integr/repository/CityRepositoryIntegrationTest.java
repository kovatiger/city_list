package com.example.test_task.integr.repository;

import com.example.test_task.integr.BaseRepositoryTest;
import com.example.test_task.model.City;
import com.example.test_task.model.Country;
import com.example.test_task.repository.CityRepository;
import com.example.test_task.repository.CountryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DisplayName("City repository tests.")
public class CityRepositoryIntegrationTest extends BaseRepositoryTest {
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountryRepository countryRepository;

    @Test
    @DisplayName("SUCCESSFUL find city by city name.")
    void findByCityName_citySaved_returnCity() {
        City city = new City();
        city.setCity("TestCity");
        Country country = new Country();
        country.setCountryName("TestCountry");
        country.setLogoName("TC");
        countryRepository.save(country);
        city.setCountry(country);
        cityRepository.saveAndFlush(city);

        Optional<City> foundByCityName = cityRepository.findByCity("TestCity");

        assertTrue(foundByCityName.isPresent());
        assertEquals(city.getCity(), foundByCityName.get().getCity());
        assertEquals(city.getCountry().getCountryName(), foundByCityName.get().getCountry().getCountryName());
        assertEquals(city.getCountry().getLogoName(), foundByCityName.get().getCountry().getLogoName());
    }

    @Test
    @DisplayName("SUCCESSFUL find city by city name.")
    void findByCityName_cityExists_returnCity() {
        Optional<City> foundByCityName = cityRepository.findByCity("Minsk");

        assertTrue(foundByCityName.isPresent());
        assertEquals("Belarus", foundByCityName.get().getCountry().getCountryName());
    }

    @Test
    @DisplayName("FAILED find city by city name.")
    void findByCityName_cityNotSaved_returnNull() {
        Optional<City> city = cityRepository.findByCity("TestCity");

        assertFalse(city.isPresent());
    }

    @Test
    @DisplayName("SUCCESSFUL find all cities.")
    void findAll_cityExist_returnCity() {
        Pageable page = PageRequest.of(0, 2);

        Page<City> cities = cityRepository.findAll(page);

        assertNotNull(cities);
        assertEquals(2, cities.get().toList().size());
        assertEquals(21, cities.getTotalElements());
        assertEquals(11, cities.getTotalPages());
    }

    @Test
    @DisplayName("FAILED find all cities.")
    void findAll_cityNotExist_returnNull() {
        cityRepository.deleteAll();

        Pageable page = PageRequest.of(0, 2);
        Page<City> cities = cityRepository.findAll(page);

        assertNotNull(cities);
        assertEquals(0, cities.get().toList().size());
        assertEquals(0, cities.getTotalElements());
        assertEquals(0, cities.getTotalPages());
    }
}
