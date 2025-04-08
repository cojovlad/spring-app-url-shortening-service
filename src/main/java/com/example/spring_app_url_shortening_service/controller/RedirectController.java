package com.example.spring_app_url_shortening_service.controller;

import com.example.spring_app_url_shortening_service.entity.Url;
import com.example.spring_app_url_shortening_service.service.impl.UrlServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;

@Controller
public class RedirectController {

    private final UrlServiceImpl urlService;

    @Autowired
    public RedirectController(UrlServiceImpl urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{alias}")
    public RedirectView redirectToOriginalUrl(@PathVariable String alias) {
        Url url = urlService.getUrlByAlias(alias);
        if (url == null || !url.isActive() || (url.getExpirationDate() != null && url.getExpirationDate().isBefore(LocalDateTime.now()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found or expired");
        }
        return new RedirectView(url.getOriginalUrl());
    }
}
