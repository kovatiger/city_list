package com.example.test_task.service;

import com.example.test_task.model.City;
import org.springframework.data.domain.Page;

/**
 * Service for working with cities
 */
public interface CityService {

    /**
     * Method for getting city by city name
     *
     * @param cityName name of city
     * @return {@link City} found city
     */
    City findCityByCityName(String cityName);

    /**
     * Method for getting cities using pagination
     *
     * @param pageSize   size of page
     * @param pageNumber number of page
     * @return {@link Page<City>} page of cities
     */
    Page<City> findAllCities(int pageNumber, int pageSize);

    /**
     * Method for updating city name
     *
     * @param city        city that name should be updated
     * @param newCityName new name of the city
     */
    void updateCityName(City city, String newCityName);
}