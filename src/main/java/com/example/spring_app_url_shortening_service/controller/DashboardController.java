package com.example.spring_app_url_shortening_service.controller;

import com.example.spring_app_url_shortening_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller responsible for handling user dashboard interactions, such as viewing the dashboard
 * and setting the user's language preference.
 */
@Controller
@RequestMapping("api/v1/dashboard")
public class DashboardController {

    private final UserService userService;

    /**
     * Constructs a new DashboardController with the given UserService dependency.
     *
     * @param userServiceInjection the service used to manage user-related operations
     */
    @Autowired
    public DashboardController(UserService userServiceInjection) {
        this.userService = userServiceInjection;
    }

    /**
     * Displays the dashboard page.
     *
     * @return the name of the dashboard view
     */
    @GetMapping
    public String dashboard() {
        return "dashboard";
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
