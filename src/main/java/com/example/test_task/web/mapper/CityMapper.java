package com.example.test_task.web.mapper;

import com.example.test_task.model.City;
import com.example.test_task.web.dto.CityWithLogoResponseDto;
import com.example.test_task.web.dto.CityByCountryNameResponseDto;
import org.springframework.data.domain.Page;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface CityMapper {

    List<CityByCountryNameResponseDto> getCitiesDto(List<City> cities);

    List<CityWithLogoResponseDto> getCitiesWithLogoDto(Page<City> page);
}