package com.app.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.app.webapp.client.UserClient;
import com.app.webapp.dto.ApiResponse;
import com.app.webapp.dto.UserDto;

@Controller
public class RegisterController {

    @Autowired
    private UserClient userClient;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDto(""));
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDto userDto, Model model) {
        ApiResponse<UserDto> response = userClient.createUser(userDto);
        if (response != null && response.getData() != null) {
            // Succès : redirige vers la page de connexion
            return "redirect:/login?success";
        } else {
            // Échec : affiche un message d'erreur
            model.addAttribute("error", "Erreur lors de l'inscription. Veuillez réessayer.");
            return "register";
        }
    }
} 