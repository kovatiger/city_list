package com.example.test_task.service.impl;

import com.example.test_task.exception.ResourceNotFoundException;
import com.example.test_task.model.User;
import com.example.test_task.repository.UserRepository;
import com.example.test_task.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public User getUserByLogin(String username) {
        return userRepository.findUserByLogin(username)
                .orElseThrow(() -> new ResourceNotFoundException("User wasn't found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userRepository.findUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User wasn't found"));
    }
}