package com.example.test_task.service.impl;

import com.example.test_task.exception.ResourceNotFoundException;
import com.example.test_task.model.City;
import com.example.test_task.model.Country;
import com.example.test_task.repository.CountryRepository;
import com.example.test_task.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Country> findAllCountries(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return countryRepository.findAll(page);
    }

    @Override
    @Transactional(readOnly = true)
    public List<City> findCitiesByCountryName(String countryName) {
        Country country = countryRepository.findByCountryName(countryName)
                .orElseThrow(() -> new ResourceNotFoundException("country wasn't found"));
        return country.getCities();
    }

    @Override
    @Transactional
    public void updateLogoName(Country country, String newFileName) {
        country.setLogoName(newFileName);
        countryRepository.save(country);
    }
}