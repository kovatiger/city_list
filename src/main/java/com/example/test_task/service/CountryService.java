package com.example.test_task.service;

import com.example.test_task.model.City;
import com.example.test_task.model.Country;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service for working with countries
 */
public interface CountryService {

    /**
     * Method for getting countries using pagination
     *
     * @param pageSize   size of page
     * @param pageNumber number of page
     * @return {@link Page<Country>} page of countries
     */
    Page<Country> findAllCountries(int pageNumber, int pageSize);

    /**
     * Method for getting all cities by country
     *
     * @param country name of country
     * @return {@link List<City>} list of cities with particular country
     */
    List<City> findCitiesByCountryName(String country);

    /**
     * Method for updating logo of country
     *
     * @param country     country that logo should be updated
     * @param newLogoName new name of the logo
     */
    void updateLogoName(Country country, String newLogoName);
}