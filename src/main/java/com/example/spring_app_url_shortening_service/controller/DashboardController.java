package com.example.spring_app_url_shortening_service.controller;

import com.example.spring_app_url_shortening_service.entity.Url;
import com.example.spring_app_url_shortening_service.entity.User;
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

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller responsible for handling user dashboard interactions, such as viewing the dashboard
 * and setting the user's language preference.
 */
@Controller
@RequestMapping("api/v1/dashboard")
public class DashboardController {

    private final UserService userService;
    private final UrlServiceImpl urlService;

    @Autowired
    public DashboardController(UserService userServiceInjection, UrlServiceImpl urlServiceInjection) {
        this.userService = userServiceInjection;
        this.urlService = urlServiceInjection;
    }

    /**
     * Displays the dashboard page.
     *
     * @return the name of the dashboard view
     */
    @GetMapping
    public String getDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Url> activeUrls = urlService.getActiveUrlsForUser(userService.getUserByUsername(userDetails.getUsername()).orElseThrow());
        model.addAttribute("activeUrls", activeUrls);
        return "dashboard";
    }

    @PostMapping("/create-url")
    public String createUrl(
            @RequestParam String originalUrl,
            @RequestParam String alias,
            @RequestParam(required = false) String expiration,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        LocalDateTime expirationDate = null;
        if (expiration != null && !expiration.isEmpty()) {
            expirationDate = LocalDateTime.parse(expiration);
        }

        try {
            urlService.createUrl(originalUrl, alias, expirationDate, userService.getUserByUsername(userDetails.getUsername()).orElseThrow());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/api/v1/dashboard";
    }

    /**
     * Updates the logged-in user's language preference.
     * This method is triggered by a POST form or request that passes a language code.
     *
     * @param code the numeric language code (e.g., 0 = English, 1 = German)
     * @param auth the authentication object containing the current user's details
     * @return redirect to the dashboard view
     * @throws Exception if updating the user's language fails
     */
    @PostMapping("/set-language")
    public String setLanguage(@RequestParam int code, Authentication auth) throws Exception {
        String username = auth.getName();
        userService.updateUserLanguage(username, code);
        return "redirect:/api/v1/dashboard";
    }
}
