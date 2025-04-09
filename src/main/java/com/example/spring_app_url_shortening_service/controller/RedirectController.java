package com.example.spring_app_url_shortening_service.controller;

import com.example.spring_app_url_shortening_service.entity.Url;
import com.example.spring_app_url_shortening_service.exception.UrlExpiredException;
import com.example.spring_app_url_shortening_service.exception.UrlInactiveException;
import com.example.spring_app_url_shortening_service.exception.UrlNotFoundException;
import com.example.spring_app_url_shortening_service.service.impl.UrlServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller responsible for handling redirection from a shortened alias to the original URL.
 * Redirects users to the original URL if the alias is valid, active, and not expired.
 * If the alias is invalid or has issues, redirects the user to the dashboard with an error message.
 */
@Controller
public class RedirectController {

    private final UrlServiceImpl urlService;

    /**
     * Constructs a new RedirectController with the given URL service.
     *
     * @param urlService the service used to retrieve and validate shortened URLs
     */
    @Autowired
    public RedirectController(UrlServiceImpl urlService) {
        this.urlService = urlService;
    }

    /**
     * Redirects the user to the original URL associated with the given alias.
     * <p>
     * If the alias is invalid, inactive, or expired, redirects to the dashboard page
     * and adds an error message as a flash attribute.
     *
     * @param alias the shortened URL alias
     * @param redirectAttributes flash attributes for passing error messages to the redirected page
     * @return RedirectView to either the original URL or the dashboard
     */
    @GetMapping("/{alias}")
    public RedirectView redirectToOriginalUrl(@PathVariable String alias, RedirectAttributes redirectAttributes) {
        try {
            Url url = urlService.getValidUrlByAlias(alias);
            return new RedirectView(url.getOriginalUrl());
        } catch (UrlNotFoundException | UrlInactiveException | UrlExpiredException e) {
            redirectAttributes.addFlashAttribute("urlRedirectError", e.getMessage());
            return new RedirectView("/api/v1/dashboard");
        }
    }
}
