package com.app.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.app.webapp.client.UserClient;
import com.app.webapp.dto.ApiResponse;
import com.app.webapp.dto.UserDto;
import com.app.webapp.security.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class RegisterController {

    @Autowired
    private UserClient userClient;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDto userDto, Model model, HttpServletRequest request) {
        System.out.println("Tentative d'inscription : " + userDto.getUsername() + " / " + userDto.getEmail());
        try {
            ApiResponse<UserDto> response = userClient.createUser(userDto);
            System.out.println("Réponse du user-service : " + response);
            if (response != null && response.getData() != null) {
                // Authentification automatique
                UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getEmail());
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, userDto.getPassword(), userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                request.getSession(true); // Force la création de la session
                // Redirection vers la complétion de profil
                return "redirect:/profile-completion/step1";
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
