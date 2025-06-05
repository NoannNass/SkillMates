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
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/register")
public String registerUser(@ModelAttribute("user") UserDto userDto, Model model) {
    System.out.println("Tentative d'inscription : " + userDto.getUsername() + " / " + userDto.getEmail());
    try {
        ApiResponse<UserDto> response = userClient.createUser(userDto);
        System.out.println("Réponse du user-service : " + response);
        if (response != null && response.getData() != null) {
            return "redirect:/login?success";
        } else {
            model.addAttribute("error", "Erreur lors de l'inscription. Veuillez réessayer.");
            return "register";
        }
    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("error", "Erreur technique : " + e.getMessage());
        return "register";
    }
}
}
