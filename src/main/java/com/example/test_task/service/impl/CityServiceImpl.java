package com.example.test_task.service.impl;

import com.example.test_task.exception.ResourceNotFoundException;
import com.example.test_task.model.City;
import com.example.test_task.repository.CityRepository;
import com.example.test_task.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    @Transactional(readOnly = true)
    public City findCityByCityName(String cityName) {
        return cityRepository.findByCity(cityName)
                .orElseThrow(() -> new ResourceNotFoundException("City wasn't found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<City> findAllCities(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return cityRepository.findAll(page);
    }

    @Override
    @Transactional
    public void updateCityName(City city, String newCityName) {
        city.setCity(newCityName);
        cityRepository.save(city);
    }
}