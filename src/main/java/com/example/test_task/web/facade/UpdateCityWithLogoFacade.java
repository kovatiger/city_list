package com.example.test_task.web.facade;

import com.example.test_task.model.City;
import com.example.test_task.model.Country;
import com.example.test_task.service.CityService;
import com.example.test_task.service.CountryService;
import com.example.test_task.service.ImageService;
import com.example.test_task.web.dto.UpdateCityAndLogoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateCityWithLogoFacade {

    private final CityService cityService;
    private final ImageService imageService;
    private final CountryService countryService;

    @Transactional
    public void updateCityWithLogo(UpdateCityAndLogoRequestDto requestDto) {
        City city = cityService.findCityByCityName(requestDto.getCurrentCityName());
        cityService.updateCityName(city, requestDto.getNewCityName());
        Country country = city.getCountry();
        String newFileName = imageService.updateLogo(requestDto.getFile(),country.getLogoName());
        countryService.updateLogoName(country,newFileName);
    }
}