package com.example.spring_app_url_shortening_service.service;

import com.example.spring_app_url_shortening_service.entity.User;
import com.example.spring_app_url_shortening_service.exception.EmailAlreadyExistsException;
import com.example.spring_app_url_shortening_service.exception.IncorrectPasswordException;
import com.example.spring_app_url_shortening_service.exception.PasswordMismatchException;
import com.example.spring_app_url_shortening_service.exception.UsernameAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing user accounts in the URL shortening application.
 * Provides operations for user creation, retrieval, and deletion.
 */
public interface UserService {
    /**
     * Creates and persists a new user in the system.
     *
     * @param user the user entity to create (must not be null)
     * @return the persisted user entity with generated ID
     * @throws com.example.spring_app_url_shortening_service.exception.UserAlreadyExistsException if a user with the same email or username already exists
     * @throws IllegalArgumentException                                                           if the provided user entity is null
     */
    User createUser(User user);

    /**
     * Retrieves all users in the system.
     *
     * @return a list of all users (possibly empty)
     */
    List<User> getAllUsers();

    /**
     * Finds a user by their unique identifier.
     *
     * @param id the user's ID (must not be null)
     * @return an Optional containing the user if found, empty otherwise
     * @throws IllegalArgumentException if the provided ID is null
     */
    Optional<User> getUser(Long id);

    /**
     * Finds a user by their email address.
     *
     * @param email the email address to search for (must not be null or empty)
     * @return an Optional containing the user if found, empty otherwise
     * @throws IllegalArgumentException if the provided email is null or empty
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for (must not be null or empty)
     * @return an Optional containing the user if found, empty otherwise
     * @throws IllegalArgumentException if the provided username is null or empty
     */
    Optional<User> getUserByUsername(String username);

    /**
     * Deletes a user by their unique identifier.
     *
     * @param id the ID of the user to delete (must not be null)
     * @throws jakarta.persistence.EntityNotFoundException if no user exists with the given ID
     * @throws IllegalArgumentException                    if the provided ID is null
     */
    void deleteUser(Long id);

    /**
     * Updates the profile information of a user.
     * <p>
     * Validates that the email and username are not already used by other users.
     * Then updates the user's profile fields (email, username, first name, last name).
     *
     * @param updatedUser a {@link User} object containing the updated user details (must include valid ID).
     * @throws EmailAlreadyExistsException    if the provided email is already in use by another user.
     * @throws UsernameAlreadyExistsException if the provided username is already in use by another user.
     */
    void updateUserProfile(User updatedUser)
            throws EmailAlreadyExistsException, UsernameAlreadyExistsException;

    /**
     * Changes the password of the user identified by the given username.
     * <p>
     * Validates that the current password is correct and that the new password matches the confirmation.
     *
     * @param username           the username of the user.
     * @param currentPassword    the user's current password.
     * @param newPassword        the new password the user wants to set.
     * @param confirmNewPassword the confirmation of the new password.
     * @throws IncorrectPasswordException if the current password is incorrect.
     * @throws PasswordMismatchException  if the new password and its confirmation do not match.
     */
    void changeUserPassword(String username, String currentPassword, String newPassword, String confirmNewPassword)
            throws IncorrectPasswordException, PasswordMismatchException;

    /**
     * Updates the language preference for a user identified by their username.
     *
     * @param username   the username of the user whose language is to be updated
     * @param languageId the ID of the new language to assign to the user
     * @throws EntityNotFoundException if the user or language does not exist
     */
    void updateUserLanguage(String username, int languageId)throws EntityNotFoundException;
}