package com.example.spring_app_url_shortening_service.controller;

import com.example.spring_app_url_shortening_service.entity.Url;
import com.example.spring_app_url_shortening_service.service.UserService;
import com.example.spring_app_url_shortening_service.service.impl.UrlServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller responsible for handling user dashboard functionality including:
 * displaying active URLs, creating new shortened URLs, and updating language preferences.
 */
@Controller
@RequestMapping("api/v1/dashboard")
public class DashboardController {

    private final UserService userService;
    private final UrlServiceImpl urlService;

    /**
     * Constructs the DashboardController with user and URL services.
     *
     * @param userServiceInjection service for user operations
     * @param urlServiceInjection service for URL operations
     */
    @Autowired
    public DashboardController(UserService userServiceInjection, UrlServiceImpl urlServiceInjection) {
        this.userService = userServiceInjection;
        this.urlService = urlServiceInjection;
    }

    /**
     * Displays the user's dashboard with a list of active URLs.
     *
     * @param userDetails the authenticated user's details
     * @param model the model to pass attributes to the view
     * @return the dashboard view name
     */
    @GetMapping
    public String getDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Url> activeUrls = urlService.getActiveUrlsForUser(
                userService.getUserByUsername(userDetails.getUsername()).orElseThrow()
        );
        model.addAttribute("activeUrls", activeUrls);
        return "dashboard";
    }

    /**
     * Handles creation of a new shortened URL.
     *
     * @param originalUrl the original long URL to be shortened
     * @param alias the custom alias for the shortened URL
     * @param expiration optional expiration date-time string (ISO format)
     * @param userDetails the authenticated user's details
     * @param redirectAttributes used to pass error messages to redirected view
     * @return redirect to the dashboard
     */
    @PostMapping("/create-url")
    public String createUrl(
            @RequestParam String originalUrl,
            @RequestParam String alias,
            @RequestParam(required = false) String expiration,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes
    ) {
        LocalDateTime expirationDate = null;
        if (expiration != null && !expiration.isEmpty()) {
            expirationDate = LocalDateTime.parse(expiration);
        }

        try {
            urlService.createUrl(
                    originalUrl,
                    alias,
                    expirationDate,
                    userService.getUserByUsername(userDetails.getUsername()).orElseThrow()
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("urlCreationError", e.getMessage());
        }

        return "redirect:/api/v1/dashboard";
    }

    /**
     * Updates the logged-in user's language preference.
     *
     * @param code language code (e.g., 0 = English, 1 = German)
     * @param auth current authenticated user
     * @return redirect to dashboard
     * @throws Exception if the language update fails
     */
    @PostMapping("/set-language")
    public String setLanguage(@RequestParam int code, Authentication auth) throws Exception {
        String username = auth.getName();
        userService.updateUserLanguage(username, code);
        return "redirect:/api/v1/dashboard";
    }
}
