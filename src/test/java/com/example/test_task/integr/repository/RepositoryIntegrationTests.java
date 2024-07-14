package com.example.test_task.integr.repository;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({UserRepositoryIntegrationTest.class, CountryRepositoryIntegrationTest.class,
        CityRepositoryIntegrationTest.class})
public class RepositoryIntegrationTests {
}
