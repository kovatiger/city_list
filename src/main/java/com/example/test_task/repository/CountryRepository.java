package com.example.test_task.repository;

import com.example.test_task.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CountryRepository extends JpaRepository<Country, UUID> {

    Page<Country> findAll(Pageable pageable);

    Optional<Country> findByCountryName(String country);
}