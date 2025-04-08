package com.example.spring_app_url_shortening_service.repository;

import com.example.spring_app_url_shortening_service.entity.Url;
import com.example.spring_app_url_shortening_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByOriginalUrl(String originalUrl);

    Optional<Url> findByAlias(String alias);

    List<Url> findByUsersContainingAndIsActive(User user, boolean isActive);

    @Modifying
    @Query("UPDATE Url u SET u.isActive = false WHERE u.expirationDate <= :now AND u.isActive = true")
    void deactivateExpiredUrls(@Param("now") LocalDateTime now);

    void deleteByUsersContainingAndIsActiveAndExpirationDateBefore(User user, boolean isActive, LocalDateTime date);
}
