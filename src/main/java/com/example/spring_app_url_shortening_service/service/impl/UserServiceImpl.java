package com.example.spring_app_url_shortening_service.service.impl;

import com.example.spring_app_url_shortening_service.entity.Language;
import com.example.spring_app_url_shortening_service.entity.User;
import com.example.spring_app_url_shortening_service.exception.UserAlreadyExistsException;
import com.example.spring_app_url_shortening_service.repository.UserRepository;
import com.example.spring_app_url_shortening_service.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
            throw new UserAlreadyExistsException("User email already exists");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
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

    /**
     * Updates the profile information of an existing user.
     * <p>
     * Performs the following validations:
     * <ul>
     *     <li>Ensures the user with the given ID exists.</li>
     *     <li>Checks that the new email is not taken by another user.</li>
     *     <li>Checks that the new username is not taken by another user.</li>
     * </ul>
     * Then updates the email, username, first name, and last name fields.
     *
     * @param updatedUser A {@link User} object containing the updated fields (must include valid ID).
     * @throws EntityNotFoundException       if the user with the given ID does not exist.
     * @throws UserAlreadyExistsException    if the provided email or username is already taken by another user.
     */
    @Transactional
    public void updateUserProfile(User updatedUser) {
        User existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Optional<User> userByEmail = userRepository.findByEmail(updatedUser.getEmail());
        if (userByEmail.isPresent() && !userByEmail.get().getId().equals(existingUser.getId())) {
            throw new UserAlreadyExistsException("Email is already taken.");
        }

        Optional<User> userByUsername = userRepository.findByUsername(updatedUser.getUsername());
        if (userByUsername.isPresent() && !userByUsername.get().getId().equals(existingUser.getId())) {
            throw new UserAlreadyExistsException("Username is already taken.");
        }

        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());

        userRepository.save(existingUser);
    }

    /**
     * Changes the password of a user identified by username.
     * <p>
     * Performs the following checks:
     * <ul>
     *     <li>Ensures the user exists.</li>
     *     <li>Validates that the current password matches the stored password.</li>
     *     <li>Checks that the new password and confirmation password match.</li>
     * </ul>
     * Then encodes and updates the user's password.
     *
     * @param username            the username of the user changing their password.
     * @param currentPassword     the user's current password.
     * @param newPassword         the new password to set.
     * @param confirmNewPassword  the confirmation of the new password.
     * @throws EntityNotFoundException   if the user does not exist.
     * @throws IllegalArgumentException if the current password is incorrect or new passwords don't match.
     */
    @Transactional
    public void changeUserPassword(String username, String currentPassword, String newPassword, String confirmNewPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        if (!newPassword.equals(confirmNewPassword)) {
            throw new IllegalArgumentException("New passwords do not match.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}