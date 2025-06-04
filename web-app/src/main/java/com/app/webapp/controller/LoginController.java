package com.app.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "success", required = false) String success,
                                Model model) {
        if (error != null) {
            model.addAttribute("error", "Identifiants invalides. Veuillez réessayer.");
        }
        if (success != null) {
            model.addAttribute("success", "Inscription réussie ! Vous pouvez vous connecter.");
        }
        return "login";
    }
} 