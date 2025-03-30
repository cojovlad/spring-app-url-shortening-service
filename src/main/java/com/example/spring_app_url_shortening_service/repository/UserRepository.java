package com.example.spring_app_url_shortening_service.repository;

import com.example.spring_app_url_shortening_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository for {@link User} entity operations.
 * Provides custom query methods for user-specific operations beyond standard CRUD.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Checks if a user exists with the given email address.
     *
     * @param email the email address to check (must not be null or empty)
     * @return true if a user exists with this email, false otherwise
     * @throws IllegalArgumentException if email is null or empty
     */
    boolean existsByEmail(String email);

    /**
     * Checks if a user exists with the given username.
     *
     * @param username the username to check (must not be null or empty)
     * @return true if a user exists with this username, false otherwise
     * @throws IllegalArgumentException if username is null or empty
     */
    boolean existsByUsername(String username);

    /**
     * Finds a user by their email address.
     *
     * @param email the email address to search for (must not be null or empty)
     * @return an Optional containing the user if found, empty otherwise
     * @throws IllegalArgumentException if email is null or empty
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for (must not be null or empty)
     * @return an Optional containing the user if found, empty otherwise
     * @throws IllegalArgumentException if username is null or empty
     */
    Optional<User> findByUsername(String username);
}