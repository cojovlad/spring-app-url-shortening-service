package com.example.spring_app_url_shortening_service.repository;

import com.example.spring_app_url_shortening_service.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
}
