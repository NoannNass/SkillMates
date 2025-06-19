package com.app.webapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.app.webapp.security.UserInfoSession;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    private final UserClient userClient;
    private final CustomUserDetailsService userDetailsService;
    private final UserInfoSession userInfoSession;

    @Autowired
    public RegisterController(UserClient userClient, CustomUserDetailsService userDetailsService, UserInfoSession userInfoSession) {
        this.userClient = userClient;
        this.userDetailsService = userDetailsService;
        this.userInfoSession = userInfoSession;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDto userDto, Model model, HttpServletRequest request) {
        logger.info("Tentative d'inscription : {} / {}", userDto.getUsername(), userDto.getEmail());
        try {
            ApiResponse<UserDto> response = userClient.createUser(userDto);
            logger.info("Réponse du user-service : {}", response);
            if (response != null && response.isSuccess() && response.getData() != null) {
                UserDto createdUser = response.getData();
                
                // Stockage des informations utilisateur dans la session
                userInfoSession.setUserId(createdUser.getId());
                userInfoSession.setEmail(createdUser.getEmail());
                userInfoSession.setUsername(createdUser.getUsername());
                logger.info("Informations utilisateur stockées en session: id={}, email={}", 
                            userInfoSession.getUserId(), userInfoSession.getEmail());
                
                // Authentification automatique
                UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getEmail());
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, userDto.getPassword(), userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                
                // Force la création de la session
                request.getSession(true); 
                
                // Redirection vers la complétion de profil
                return "redirect:/profile-completion/step1";
            } else {
                String errorMessage = response != null && response.getMessage() != null ? 
                                    response.getMessage() : "Erreur lors de l'inscription";
                logger.error("Échec de l'inscription: {}", errorMessage);
                model.addAttribute("error", errorMessage + ". Veuillez réessayer.");
                return "register";
            }
        } catch (Exception e) {
            logger.error("Erreur technique lors de l'inscription", e);
            model.addAttribute("error", "Erreur technique : " + e.getMessage());
            return "register";
        }
    }
}
