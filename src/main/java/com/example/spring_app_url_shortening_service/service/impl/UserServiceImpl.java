package com.example.spring_app_url_shortening_service.service.impl;

import com.example.spring_app_url_shortening_service.entity.User;
import com.example.spring_app_url_shortening_service.exception.UserAlreadyExistsException;
import com.example.spring_app_url_shortening_service.repository.UserRepository;
import com.example.spring_app_url_shortening_service.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link UserService} providing concrete user management operations.
 * Handles user creation, retrieval, and deletion with proper security measures.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new UserServiceImpl with required dependencies.
     *
     * @param userRepository  the user repository for database operations
     * @param passwordEncoder the password encoder for securing user credentials
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * {@inheritDoc}
     *
     * @throws UserAlreadyExistsException if either email or username already exists in the system
     */
    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("user.exists");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("username.exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     *
     * @return list of all users in the system, ordered by creation date
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * {@inheritDoc}
     *
     * @param id the ID of the user to retrieve
     * @return Optional containing the user if found, empty otherwise
     */
    @Override
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     *
     * @param email the email address to search for
     * @return Optional containing the user if found, empty otherwise
     */
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * {@inheritDoc}
     *
     * @param username the username to search for
     * @return Optional containing the user if found, empty otherwise
     */
    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * {@inheritDoc}
     *
     * @param id the ID of the user to delete
     * @throws EntityNotFoundException if no user exists with the given ID
     */
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("user.not.exists");
        }
        userRepository.deleteById(id);
    }
}