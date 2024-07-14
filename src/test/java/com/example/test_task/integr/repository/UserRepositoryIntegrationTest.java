package com.example.test_task.integr.repository;

import com.example.test_task.integr.BaseRepositoryTest;
import com.example.test_task.model.User;
import com.example.test_task.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("User repository tests.")
public class UserRepositoryIntegrationTest extends BaseRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("SUCCESSFUL find user by login.")
    void findUserByLogin_userSaved_returnUser() {
        User userEntity = new User();
        userEntity.setLogin("testUser");
        userEntity.setPassword("testUser");
        userRepository.saveAndFlush(userEntity);

        Optional<User> foundByLoginUser = userRepository.findUserByLogin("testUser");

        assertTrue(foundByLoginUser.isPresent());
        assertEquals(userEntity.getLogin(), foundByLoginUser.get().getLogin());
        assertEquals(userEntity.getPassword(), foundByLoginUser.get().getPassword());
    }

    @Test
    @DisplayName("FAILED find user by login.")
    void findUserByLogin_userNotSaved_returnNull() {
        Optional<User> loginUser = userRepository.findUserByLogin("testUser");

        assertFalse(loginUser.isPresent());
    }
}