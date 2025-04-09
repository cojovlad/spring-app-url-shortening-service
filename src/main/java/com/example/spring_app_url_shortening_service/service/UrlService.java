package com.example.spring_app_url_shortening_service.service;

import com.example.spring_app_url_shortening_service.entity.Url;
import com.example.spring_app_url_shortening_service.entity.User;
import com.example.spring_app_url_shortening_service.exception.AliasAlreadyExistsException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface defining the core operations for managing URL shortening logic.
 */
public interface UrlService {

    /**
     * Creates a new shortened URL with the given parameters.
     *
     * @param originalUrl the original long URL to shorten
     * @param alias the custom alias for the shortened URL
     * @param expiration the optional expiration date/time for the URL
     * @param user the user who owns the URL
     * @return the created {@link Url} object
     * @throws AliasAlreadyExistsException if the provided alias already exists
     */
    Url createUrl(String originalUrl, String alias, LocalDateTime expiration, User user) throws AliasAlreadyExistsException;

    /**
     * Retrieves all active (non-expired and active flag set to true) URLs for the specified user.
     *
     * @param user the user whose active URLs should be retrieved
     * @return a list of active {@link Url} objects
     */
    List<Url> getActiveUrlsForUser(User user);

    /**
     * Retrieves a URL by its alias without validating its status (e.g., active or expired).
     *
     * @param alias the alias of the shortened URL
     * @return the corresponding {@link Url} if found, or null otherwise
     */
    Url getUrlByAlias(String alias);

    /**
     * Marks all expired URLs as inactive.
     * Should be scheduled to run periodically.
     */
    void deactivateExpiredUrls();

    /**
     * Deletes all expired URLs from the database.
     * Should be scheduled to run periodically.
     */
    void deleteExpiredUrls();

    /**
     * Retrieves a URL by alias only if it is valid (exists, active, and not expired).
     *
     * @param alias the alias of the shortened URL
     * @return the valid {@link Url}
     * @throws com.example.spring_app_url_shortening_service.exception.UrlNotFoundException if the alias does not exist
     * @throws com.example.spring_app_url_shortening_service.exception.UrlInactiveException if the URL is inactive
     * @throws com.example.spring_app_url_shortening_service.exception.UrlExpiredException if the URL has expired
     */
    Url getValidUrlByAlias(String alias);
}
