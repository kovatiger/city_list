package com.example.test_task.service;

import com.example.test_task.web.dto.CityWithLogoResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service for working with images
 */
public interface ImageService {

    /**
     * Method for getting image by logo name
     *
     * @param logoName name of logo
     * @return image
     */
    String getImageUrlByLogoName(String logoName);

    /**
     * Method for getting images for list of cities
     *
     * @param citiesWithLogoDto the list dtos with city without image urls
     * @return {@link List< CityWithLogoResponseDto >} the list dtos with images
     */
    List<CityWithLogoResponseDto> getListOfCitiesWithLogos(List<CityWithLogoResponseDto> citiesWithLogoDto);

    /**
     * Method for updating logo name
     *
     * @param file     file that should be downloaded
     * @param logoName old logo name
     * @return new generated logo name
     */
    String updateLogo(MultipartFile file, String logoName);
}