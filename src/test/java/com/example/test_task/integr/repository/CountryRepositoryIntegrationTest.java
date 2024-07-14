package com.example.test_task.integr.repository;

import com.example.test_task.integr.BaseRepositoryTest;
import com.example.test_task.model.Country;
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

@DataJpaTest
@DisplayName("Country repository tests.")
public class CountryRepositoryIntegrationTest extends BaseRepositoryTest {
    @Autowired
    private CountryRepository countryRepository;

    @Test
    @DisplayName("SUCCESSFUL find country by country name.")
    void findByCountryName_countrySaved_returnCountry() {
        Country country = new Country();
        country.setCountryName("TestCountry");
        country.setLogoName("TC");
        countryRepository.saveAndFlush(country);

        Optional<Country> foundByCountryName = countryRepository.findByCountryName("TestCountry");

        assertTrue(foundByCountryName.isPresent());
        assertEquals(country.getCountryName(), foundByCountryName.get().getCountryName());
        assertEquals(country.getLogoName(), foundByCountryName.get().getLogoName());
    }

    @Test
    @DisplayName("SUCCESSFUL find country by country name.")
    void findByCountryName_countryExists_returnCountry() {
        Optional<Country> foundByCountryName = countryRepository.findByCountryName("Belarus");

        assertTrue(foundByCountryName.isPresent());
        assertEquals("Belarus", foundByCountryName.get().getCountryName());
        assertEquals("by.png", foundByCountryName.get().getLogoName());
    }

    @Test
    @DisplayName("FAILED find country by country name.")
    void findByCountryName_countryNotSaved_returnNull() {
        Optional<Country> country = countryRepository.findByCountryName("TestCountry");

        assertFalse(country.isPresent());
    }

    @Test
    @DisplayName("SUCCESSFUL find all country.")
    void findAll_countryExist_returnCountry() {
        Pageable page = PageRequest.of(0, 2);

        Page<Country> countries = countryRepository.findAll(page);

        assertNotNull(countries);
        assertEquals(2, countries.get().toList().size());
        assertEquals(7, countries.getTotalElements());
        assertEquals(4, countries.getTotalPages());
    }

    @Test
    @DisplayName("FAILED find all countries.")
    void findAll_countryNotExist_returnNull() {
        countryRepository.deleteAll();

        Pageable page = PageRequest.of(0, 2);
        Page<Country> countries = countryRepository.findAll(page);

        assertNotNull(countries);
        assertEquals(0, countries.get().toList().size());
        assertEquals(0, countries.getTotalElements());
        assertEquals(0, countries.getTotalPages());
    }
}