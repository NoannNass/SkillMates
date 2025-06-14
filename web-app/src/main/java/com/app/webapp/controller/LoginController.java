package com.app.webapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.webapp.client.UserClient;
import com.app.webapp.dto.ApiResponse;
import com.app.webapp.dto.UserDto;
import com.app.webapp.dto.UserInfoSession;
import com.app.webapp.security.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    private final UserClient userClient;
    private final CustomUserDetailsService userDetailsService;
    private final UserInfoSession userInfoSession;
    
    @Autowired
    public LoginController(UserClient userClient, CustomUserDetailsService userDetailsService, UserInfoSession userInfoSession) {
        this.userClient = userClient;
        this.userDetailsService = userDetailsService;
        this.userInfoSession = userInfoSession;
    }

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
        
        // Si l'utilisateur est déjà authentifié, rediriger vers le dashboard
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            logger.info("Utilisateur déjà authentifié, redirection vers le dashboard");
            return "redirect:/dashboard";
        }
        
        return "login";
    }
    
    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String email, 
                              @RequestParam("password") String password,
                              HttpServletRequest request,
                              Model model) {
        
        logger.info("Tentative de connexion pour: {}", email);
        
        try {
            // S'assurer que toute session précédente est nettoyée avant de créer une nouvelle
            HttpSession existingSession = request.getSession(false);
            if (existingSession != null) {
                existingSession.invalidate();
                logger.info("Session précédente invalidée");
            }
            
            // Nettoyer le contexte de sécurité
            SecurityContextHolder.clearContext();
            
            // Réinitialiser les données utilisateur en session
            userInfoSession.clear();
            
            // Récupérer les informations utilisateur à jour
            ApiResponse<UserDto> response = userClient.getUserByEmail(email);
            if (response != null && response.isSuccess() && response.getData() != null) {
                UserDto user = response.getData();
                logger.info("Utilisateur trouvé: id={}, email={}, username={}", 
                           user.getId(), user.getEmail(), user.getUsername());
                
                // Créer une nouvelle session HTTP
                HttpSession session = request.getSession(true);
                logger.info("Nouvelle session créée: {}", session.getId());
                
                // Mettre à jour la session avec les données fraîches de l'utilisateur
                userInfoSession.setUserId(user.getId());
                userInfoSession.setEmail(user.getEmail());
                userInfoSession.setUsername(user.getUsername());
                
                // Authentifier manuellement
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
                
                logger.info("Utilisateur authentifié avec succès, session ID: {}, userId: {}", 
                          session.getId(), userInfoSession.getUserId());
                
                // Redirection vers le dashboard
                return "redirect:/dashboard";
            } else {
                logger.warn("Échec de l'authentification : utilisateur non trouvé");
                model.addAttribute("error", "Identifiants invalides. Veuillez réessayer.");
                return "login";
            }
        } catch (Exception e) {
            logger.error("Erreur lors de l'authentification", e);
            model.addAttribute("error", "Erreur technique lors de la connexion.");
            return "login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Nettoyer les informations de session
        userInfoSession.clear();
        
        // Nettoyer le contexte de sécurité
        SecurityContextHolder.clearContext();
        
        // Invalider la session HTTP
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            logger.info("Session invalidée lors de la déconnexion");
        }
        
        logger.info("Déconnexion effectuée");
        
        // Redirection vers la page d'accueil
        return "redirect:/";
    }
} 