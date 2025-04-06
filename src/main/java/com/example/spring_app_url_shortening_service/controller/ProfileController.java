package com.example.spring_app_url_shortening_service.controller;

import com.example.spring_app_url_shortening_service.entity.User;
import com.example.spring_app_url_shortening_service.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final UserService userService;

    /**
     * Constructs a ProfileController with required user service.
     *
     * @param userService the service that handles user data operations
     */
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the profile page with the authenticated user's data.
     *
     * @param userDetails the current logged-in user details
     * @param model       the model to pass attributes to the view
     * @return the profile page view
     */
    @GetMapping
    public String showProfile(@AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
        userService.getUserByUsername(userDetails.getUsername()).ifPresent(user -> model.addAttribute("user", user));
        return "profile";
    }

    /**
     * Updates the user's profile information.
     * On success or failure, sets flash attributes and redirects back to the profile page.
     *
     * @param updatedUser        the updated user object from the form
     * @param redirectAttributes container to pass flash messages across the redirect
     * @return redirect to the profile page
     */
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User updatedUser,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserProfile(updatedUser);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("updateError", e.getMessage());
        }
        return "redirect:/api/v1/profile";
    }

    /**
     * Handles password change request for the authenticated user.
     * Sets success or error message via flash attributes.
     *
     * @param currentPassword     the user's current password
     * @param newPassword         the new password
     * @param confirmNewPassword  confirmation of the new password
     * @param userDetails         the current logged-in user
     * @param redirectAttributes  container to pass flash messages across the redirect
     * @return redirect to the profile page
     */
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmNewPassword,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.changeUserPassword(userDetails.getUsername(), currentPassword, newPassword, confirmNewPassword);
            redirectAttributes.addFlashAttribute("success", "Password changed successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("passwordError", e.getMessage());
        }
        return "redirect:/api/v1/profile";
    }
}
