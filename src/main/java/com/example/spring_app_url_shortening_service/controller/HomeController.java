package com.example.spring_app_url_shortening_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling root path ("/") requests and redirecting to appropriate views.
 * Serves as the entry point for the application's web interface.
 */
@Controller
public class HomeController {

    /**
     * Handles requests to the root URL ("/") and redirects to the login page.
     *
     * @return A redirect instruction to the authentication login page
     * @see com.example.spring_app_url_shortening_service.controller.AuthController#showLoginForm
     */
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/api/v1/auth/login";
    }
}