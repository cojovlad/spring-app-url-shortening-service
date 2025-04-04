package com.example.spring_app_url_shortening_service.controller;

import com.example.spring_app_url_shortening_service.entity.User;
import com.example.spring_app_url_shortening_service.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showProfile(@AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
        userService.getUserByUsername(userDetails.getUsername()).ifPresent(user -> model.addAttribute("user", user));
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User updatedUser,
                                Model model) {
        try {
            userService.updateUserProfile(updatedUser);
            model.addAttribute("success", "Profile updated successfully.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/api/v1/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmNewPassword,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {
        try {
            userService.changeUserPassword(userDetails.getUsername(), currentPassword, newPassword, confirmNewPassword);
            model.addAttribute("success", "Password changed successfully.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/api/v1/profile";
    }
}
