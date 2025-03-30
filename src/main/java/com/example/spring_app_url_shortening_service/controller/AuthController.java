package com.example.spring_app_url_shortening_service.controller;

import com.example.spring_app_url_shortening_service.entity.User;
import com.example.spring_app_url_shortening_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller handling authentication related requests including login and registration.
 * Provides both web interface and API endpoints for user authentication.
 */
@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;

    /**
     * Constructs an AuthController with the specified UserService.
     * @param userServiceInjection the user service to be used for user operations
     */
    @Autowired
    public AuthController(UserService userServiceInjection) {
        this.userService = userServiceInjection;
    }

    /**
     * Displays the login form with optional status messages.
     * @param rememberMe optional remember-me parameter
     * @param error optional error parameter indicating failed login
     * @param logout optional logout parameter indicating successful logout
     * @param registered optional parameter indicating successful registration
     * @param authentication current authentication object
     * @param model the model to add attributes for the view
     * @return the login view name or redirects to dashboard if already authenticated
     */
    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(required = false) String rememberMe,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registered", required = false) String registered,
            Authentication authentication,
            Model model) {

        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/dashboard";
        }

        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }
        if (registered != null) {
            model.addAttribute("message", "Registration successful! Please login");
        }
        return "login";
    }

    /**
     * Displays the user registration form.
     * @param model the model to add attributes for the view
     * @return the registration view name
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Processes user registration form submission.
     * @param user the user entity populated from the form
     * @param model the model to add attributes for the view
     * @return redirect to login page on success or registration page with error
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.createUser(user);
            return "redirect:/api/v1/auth/login?registered";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    /**
     * REST API endpoint for user registration.
     * @param user the user data in JSON format
     * @return ResponseEntity with created user or error message
     */
    @PostMapping(value = "/register/api", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<?> registerUserApi(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}