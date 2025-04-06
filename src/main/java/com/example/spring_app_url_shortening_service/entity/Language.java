package com.example.spring_app_url_shortening_service.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents a language entity in the URL shortening application.
 * Defines supported languages for user interface localization.
 *
 * <p>This entity maps to the "languages" table in the database and is used
 * to store available language options for user preferences.</p>
 */
@Entity
@Data
@Table(name = "languages")
public class Language {

    /**
     * Unique identifier for the language.
     * Automatically generated by the database using identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ISO language code representing the language (e.g., "en", "fr", "de").
     * Must be unique and non-null.
     *
     * <p>Follows standard language code conventions (typically ISO 639-1).</p>
     */
    @Column(unique = true, nullable = false)
    private String languageCode;
}