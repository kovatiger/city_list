package com.example.test_task.web;

import com.example.test_task.model.Country;
import com.example.test_task.web.dto.CountryDto;
import org.springframework.data.domain.Page;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "Spring")
public interface CountryMapper {

    List<CountryDto> getCountryDto(Page<Country> page);
}