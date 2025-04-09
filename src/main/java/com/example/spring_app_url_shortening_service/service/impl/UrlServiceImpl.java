package com.example.spring_app_url_shortening_service.service.impl;

import com.example.spring_app_url_shortening_service.entity.Url;
import com.example.spring_app_url_shortening_service.entity.User;
import com.example.spring_app_url_shortening_service.exception.AliasAlreadyExistsException;
import com.example.spring_app_url_shortening_service.exception.UrlExpiredException;
import com.example.spring_app_url_shortening_service.exception.UrlInactiveException;
import com.example.spring_app_url_shortening_service.exception.UrlNotFoundException;
import com.example.spring_app_url_shortening_service.repository.UrlRepository;
import com.example.spring_app_url_shortening_service.repository.UserRepository;
import com.example.spring_app_url_shortening_service.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of {@link UrlService} that provides core business logic
 * for managing URL creation, validation, expiration, and cleanup.
 */
@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final UserRepository userRepository;

    /**
     * Constructs the service with required dependencies.
     *
     * @param urlRepository repository for URL entities
     * @param userRepository repository for User entities
     */
    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository, UserRepository userRepository) {
        this.urlRepository = urlRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates and saves a new shortened URL associated with the given user.
     *
     * @param originalUrl the original long URL
     * @param alias the custom alias for the shortened URL
     * @param expiration optional expiration date/time
     * @param user the user creating the URL
     * @return the saved URL entity
     * @throws AliasAlreadyExistsException if the alias is already in use
     */
    @Override
    @Transactional
    public Url createUrl(String originalUrl, String alias, LocalDateTime expiration, User user)
            throws AliasAlreadyExistsException {
        if (urlRepository.findByAlias(alias).isPresent()) {
            throw new AliasAlreadyExistsException("Alias already exists");
        }

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setAlias(alias);
        url.setExpirationDate(expiration);

        url.getUsers().add(user);
        user.getUrls().add(url);

        Url savedUrl = urlRepository.save(url);
        userRepository.save(user);

        return savedUrl;
    }

    /**
     * Retrieves all active URLs for a given user.
     *
     * @param user the user whose URLs are to be retrieved
     * @return list of active URLs
     */
    @Override
    @Transactional
    public List<Url> getActiveUrlsForUser(User user) {
        return urlRepository.findByUsersContainingAndIsActive(user, true);
    }

    /**
     * Finds a URL by its alias.
     *
     * @param alias the unique alias
     * @return the matching URL, or null if not found
     */
    @Override
    @Transactional
    public Url getUrlByAlias(String alias) {
        return urlRepository.findByAlias(alias).orElse(null);
    }

    /**
     * Retrieves a valid URL by alias and verifies it is active and not expired.
     *
     * @param alias the unique alias of the URL
     * @return the validated URL
     * @throws UrlNotFoundException if no URL is found with the alias
     * @throws UrlInactiveException if the URL is inactive
     * @throws UrlExpiredException if the URL is expired
     */
    @Override
    @Transactional
    public Url getValidUrlByAlias(String alias) {
        Url url = urlRepository.findByAlias(alias)
                .orElseThrow(() -> new UrlNotFoundException("URL not found for alias: " + alias));

        if (!url.isActive()) {
            throw new UrlInactiveException("This link is no longer active");
        }

        if (url.isExpired()) {
            throw new UrlExpiredException("Link expired and will be available after today");
        }

        return url;
    }

    /**
     * Scheduled job to deactivate expired URLs every hour.
     */
    @Override
    @Scheduled(cron = "0 0 * * * *")
    public void deactivateExpiredUrls() {
        urlRepository.deactivateExpiredUrls(LocalDateTime.now());
    }

    /**
     * Scheduled job to permanently delete expired URLs every Sunday at midnight.
     */
    @Override
    @Scheduled(cron = "0 0 * * * Sun")
    @Transactional
    public void deleteExpiredUrls() {
        urlRepository.deleteExpiredUrls(LocalDateTime.now());
    }
}