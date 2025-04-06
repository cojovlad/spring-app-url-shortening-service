package com.example.spring_app_url_shortening_service.repository;

import com.example.spring_app_url_shortening_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link Role} entity operations.
 * Provides standard CRUD operations and JPA functionality for Role management.
 *
 * <p>This repository extends {@link JpaRepository} which provides:
 * <ul>
 *   <li>Basic CRUD operations (save, findById, findAll, etc.)</li>
 *   <li>Pagination and sorting support</li>
 *   <li>Batch operations</li>
 *   <li>Automatic transaction management</li>
 * </ul>
 *
 * <p>Additional custom query methods can be declared following Spring Data JPA naming conventions.</p>
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}