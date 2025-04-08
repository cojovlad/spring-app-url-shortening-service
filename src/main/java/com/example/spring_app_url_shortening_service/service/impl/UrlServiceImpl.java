package com.example.spring_app_url_shortening_service.service.impl;

import com.example.spring_app_url_shortening_service.entity.Url;
import com.example.spring_app_url_shortening_service.entity.User;
import com.example.spring_app_url_shortening_service.exception.AliasAlreadyExistsException;
import com.example.spring_app_url_shortening_service.repository.UrlRepository;
import com.example.spring_app_url_shortening_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UrlServiceImpl {
    private final UrlRepository urlRepository;
    private final UserRepository userRepository;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepositoryInjection, UserRepository userRepositoryInjection){
        this.urlRepository = urlRepositoryInjection;
        this.userRepository = userRepositoryInjection;
    }

    @Transactional
    public Url createUrl(String originalUrl, String alias, LocalDateTime expiration, User user)
            throws AliasAlreadyExistsException {

        if(urlRepository.findByAlias(alias).isPresent()) {
            throw new AliasAlreadyExistsException("Alias already exists");
        }

        if (alias != null && urlRepository.findByAlias(alias).isPresent()) {
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

    @Scheduled(cron = "0 0 * * * *")
    public void deactivateExpiredUrls() {
        urlRepository.deactivateExpiredUrls(LocalDateTime.now());
    }

    @Transactional
    // Add missing method implementations
    public List<Url> getActiveUrlsForUser(User user) {
        return urlRepository.findByUsersContainingAndIsActive(user, true);
    }

    public Url getUrlByAlias(String alias) {
        return urlRepository.findByAlias(alias).orElse(null);
    }

    @Transactional
    public void cleanupExpiredUrls(User user) {
        urlRepository.deleteByUsersContainingAndIsActiveAndExpirationDateBefore(
                user, false, LocalDateTime.now()
        );
    }
}
