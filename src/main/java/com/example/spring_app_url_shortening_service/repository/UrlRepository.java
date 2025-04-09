package com.example.spring_app_url_shortening_service.repository;

import com.example.spring_app_url_shortening_service.entity.Url;
import com.example.spring_app_url_shortening_service.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD and custom operations on {@link Url} entities.
 */
@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    /**
     * Finds a URL by its alias.
     *
     * @param alias the unique alias of the shortened URL
     * @return an Optional containing the Url if found, or empty if not
     */
    Optional<Url> findByAlias(String alias);

    /**
     * Finds all active or inactive URLs associated with a specific user.
     *
     * @param user the user entity
     * @param isActive true to retrieve active URLs, false for inactive
     * @return list of URLs matching the criteria
     */
    List<Url> findByUsersContainingAndIsActive(User user, boolean isActive);

    /**
     * Marks all expired and still-active URLs as inactive.
     *
     * @param now the current date/time used to compare against expirationDate
     */
    @Modifying
    @Transactional
    @Query("UPDATE Url u SET u.isActive = false WHERE u.expirationDate <= :now AND u.isActive = true")
    void deactivateExpiredUrls(@Param("now") LocalDateTime now);

    /**
     * Permanently deletes all expired URLs from the database.
     *
     * @param now the current date/time used to compare against expirationDate
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Url u WHERE u.expirationDate <= :now")
    void deleteExpiredUrls(@Param("now") LocalDateTime now);
}
