package com.example.test_task.service;

import com.example.test_task.model.User;

import java.util.UUID;

/**
 * Service for working with users
 */
public interface UserService {

    /**
     * Method for getting user by user login
     *
     * @param login user login
     * @return {@link User} user with particular login
     */
    User getUserByLogin(String login);

    /**
     * Method for getting user by user id
     *
     * @param id user id
     * @return {@link User} user with particular id
     */
    User getUserById(UUID id);
}