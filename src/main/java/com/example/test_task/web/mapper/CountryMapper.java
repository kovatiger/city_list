package com.example.test_task.web.mapper;

import com.example.test_task.model.Country;
import com.example.test_task.web.dto.CountryResponseDto;
import org.springframework.data.domain.Page;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface CountryMapper {

    List<CountryResponseDto> getCountryDto(Page<Country> page);
}