package com.example.spring_app_url_shortening_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("api/v1/dashboard")
public class DashboardController {

    @GetMapping
    public String dashboard() {
        return "dashboard";
    }
}
