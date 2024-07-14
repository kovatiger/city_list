package com.example.test_task.unit.service.impl;

import com.example.test_task.exception.ResourceNotFoundException;
import com.example.test_task.model.User;
import com.example.test_task.repository.UserRepository;
import com.example.test_task.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private UUID userId;
    private String userLogin;
    private User user;

    @BeforeEach
    void init() {

        userId = UUID.randomUUID();

        userLogin = "userLogin";

        user = createUserWithDefaultProps();
    }

    @Test
    void getUserByLogin_userExists_returnUser() {
        when(userRepository.findUserByLogin(userLogin)).thenReturn(Optional.of(user));

        User actualUser = userService.getUserByLogin(userLogin);

        verify(userRepository, times(1)).findUserByLogin(userLogin);
        assertEquals(user, actualUser);
    }

    @Test
    void getUserByLogin_userDoesntExist_throwsException() {
        when(userRepository.findUserByLogin(userLogin)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserByLogin(userLogin));

        verify(userRepository, times(1)).findUserByLogin(userLogin);
    }

    @Test
    void getUserById_userExists_returnUser() {
        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));

        User actualUser = userService.getUserById(userId);

        verify(userRepository, times(1)).findUserById(userId);
        assertEquals(user, actualUser);
    }

    @Test
    void getUserById_userDoesntExist_throwsException() {
        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));

        verify(userRepository, times(1)).findUserById(userId);
    }

    private User createUserWithDefaultProps() {
        return User.builder()
                .id(userId)
                .login(userLogin)
                .build();
    }
}