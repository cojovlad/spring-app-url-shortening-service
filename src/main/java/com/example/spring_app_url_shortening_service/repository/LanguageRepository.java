package com.example.spring_app_url_shortening_service.repository;

import com.example.spring_app_url_shortening_service.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link Language} entity operations.
 * Provides standard CRUD operations and JPA functionality for Language management.
 *
 * <p>Extends {@link org.springframework.data.jpa.repository.JpaRepository} to inherit:
 * <ul>
 *   <li>Basic CRUD operations (save, findById, findAll, deleteById, etc.)</li>
 *   <li>Pagination and sorting support</li>
 *   <li>Batch operations</li>
 *   <li>Automatic transaction management</li>
 * </ul>
 *
 * <p>Custom query methods can be added following Spring Data JPA naming conventions,
 * for example: {@code findByLanguageCode(String code)}.</p>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see com.example.spring_app_url_shortening_service.entity.Language
 */
@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
}