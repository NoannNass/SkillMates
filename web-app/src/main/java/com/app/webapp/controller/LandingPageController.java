package com.app.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingPageController {

    @GetMapping("/")
    public String landingPage() {
        return "landing"; // Ce nom doit correspondre à un template (ex: landing.html)
    }
} 