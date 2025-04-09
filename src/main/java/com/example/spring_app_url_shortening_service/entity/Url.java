package com.example.spring_app_url_shortening_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a shortened URL entry in the system.
 * Contains metadata such as original URL, alias, expiration, and active status.
 */
@Entity
@Table(name = "urls")
@Data
public class Url {

    /**
     * Primary key of the URL entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The original (long) URL to be shortened.
     */
    @Column(nullable = false, length = 2048)
    private String originalUrl;

    /**
     * Unique alias used as the shortened identifier.
     */
    @Column(nullable = false, unique = true)
    private String alias;

    /**
     * Optional expiration date/time for the shortened URL.
     */
    private LocalDateTime expirationDate;

    /**
     * Timestamp of when the shortened URL was created.
     */
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Indicates whether the URL is active.
     */
    private boolean isActive = true;

    /**
     * Users associated with this URL (many-to-many relationship).
     */
    @ManyToMany(mappedBy = "urls", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> users = new ArrayList<>();

    /**
     * Checks if the URL has expired based on the current time.
     *
     * @return true if expired, false otherwise
     */
    public boolean isExpired() {
        return getExpirationDate() != null &&
                getExpirationDate().isBefore(LocalDateTime.now());
    }

    /**
     * Checks if the URL is both active and not expired.
     *
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return isActive() && !isExpired();
    }
}
