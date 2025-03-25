package com.example.spring_app_url_shortening_service.service;

import com.example.spring_app_url_shortening_service.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    List<User> getAllUsers();
    Optional<User> getUser(Long id);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByUsername(String username);
    void deleteUser(Long id);
}