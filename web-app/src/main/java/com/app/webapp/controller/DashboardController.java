package com.app.webapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.webapp.client.UserClient;
import com.app.webapp.dto.LearningObjectiveDTO;
import com.app.webapp.dto.UserDto;
import com.app.webapp.security.UserInfoSession;

@Controller
public class DashboardController {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    
    private final UserClient userClient;
    private final UserInfoSession userInfoSession;
    
    public DashboardController(UserClient userClient, UserInfoSession userInfoSession) {
        this.userClient = userClient;
        this.userInfoSession = userInfoSession;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        String userId = userInfoSession.getUserId();
        String username = userInfoSession.getUsername();

        // Si la session est vide mais que l'utilisateur est authentifié, recharger les infos
        if (userId == null) {
            var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User userDetails) {
                String email = userDetails.getUsername();
                try {
                    var response = userClient.getUserByEmail(email);
                    if (response != null && response.getData() != null) {
                        var user = response.getData();
                        userInfoSession.setUserId(user.getId());
                        userInfoSession.setEmail(user.getEmail());
                        userInfoSession.setUsername(user.getUsername());
                        userId = user.getId();
                        username = user.getUsername();
                    }
                } catch (Exception e) {
                    logger.error("Erreur lors de la récupération des données utilisateur après reconnexion", e);
                }
            }
        }

        logger.info("Accès au dashboard pour l'utilisateur: userId={}, username={}", userId, username);
        
        // Initialiser les variables avec des valeurs vides
        String fullName = "";
        String bio = "";
        List<String> skills = new ArrayList<>();
        List<LearningObjectiveDTO> learningObjectives = new ArrayList<>();
        
        // Si l'ID utilisateur est disponible, récupérer les données réelles
        if (userId != null) {
            try {
                // Récupération des données fraîches depuis le service utilisateur
                var response = userClient.getUserById(userId);
                if (response != null && response.getData() != null) {
                    UserDto user = response.getData();
                    
                    // Mettre à jour les informations de session avec les données fraîches
                    userInfoSession.setUserId(user.getId());
                    userInfoSession.setEmail(user.getEmail());
                    userInfoSession.setUsername(user.getUsername());
                    
                    // Utiliser le nom réel de l'utilisateur ou son username
                    if (user.getUsername() != null) {
                        fullName = user.getUsername();
                    }
                    
                    // Récupérer la bio de l'utilisateur
                    if (user.getBio() != null) {
                        bio = user.getBio();
                    }
                    
                    // Récupérer les compétences réelles
                    if (user.getSkillTags() != null && !user.getSkillTags().isEmpty()) {
                        skills = user.getSkillTags().stream()
                                .map(skill -> skill.getName())
                                .toList();
                    }
                    
                    // Récupérer les objectifs d'apprentissage réels
                    if (user.getLearningObjectives() != null && !user.getLearningObjectives().isEmpty()) {
                        learningObjectives = new ArrayList<>(user.getLearningObjectives());
                    }
                    
                    logger.info("Données utilisateur récupérées: fullName={}, objectifs={}, userId={}", 
                              fullName, learningObjectives.size(), user.getId());
                } else {
                    logger.warn("Impossible de récupérer les données de l'utilisateur avec l'ID: {}", userId);
                }
            } catch (Exception e) {
                logger.error("Erreur lors de la récupération des données utilisateur", e);
            }
        } else {
            logger.warn("Aucun ID utilisateur en session");
        }
        
        model.addAttribute("fullName", fullName);
        model.addAttribute("bio", bio);
        model.addAttribute("skills", skills);
        model.addAttribute("learningObjectives", learningObjectives);
        
        return "dashboard";
    }

    @GetMapping("/profil")
    public String profil(Model model) {
        String userId = userInfoSession.getUserId();
        String username = userInfoSession.getUsername();
        
        logger.info("Accès au profil pour l'utilisateur: userId={}, username={}", userId, username);
        
        // Par défaut, utiliser des valeurs de démonstration
        String fullName = "Thomas Dubois";
        String email = "thomas.dubois@email.com";
        String bio = "Développeur web passionné par l'UX/UI et l'accessibilité. Actuellement en apprentissage de React et TypeScript.";
        List<String> skills = List.of("Développement Web", "UX/UI Design", "Accessibilité", "JavaScript", "React", "TypeScript");
        List<LearningObjectiveDTO> learningObjectives = new ArrayList<>();
        
        // Ajouter des objectifs de démonstration par défaut
        learningObjectives.add(new LearningObjectiveDTO(null, "Maîtriser React et ses hooks", null, 65));
        learningObjectives.add(new LearningObjectiveDTO(null, "Créer un portfolio de projets UX/UI", null, 30));
        
        // Si l'ID utilisateur est disponible, récupérer les données réelles
        if (userId != null) {
            try {
                // Récupération des données fraîches depuis le service utilisateur
                var response = userClient.getUserById(userId);
                if (response != null && response.getData() != null) {
                    UserDto user = response.getData();
                    
                    // Mettre à jour les informations de session avec les données fraîches
                    userInfoSession.setUserId(user.getId());
                    userInfoSession.setEmail(user.getEmail());
                    userInfoSession.setUsername(user.getUsername());
                    
                    // Utiliser les données réelles de l'utilisateur
                    if (user.getUsername() != null) {
                        fullName = user.getUsername();
                    }
                    
                    if (user.getEmail() != null) {
                        email = user.getEmail();
                    }
                    
                    if (user.getBio() != null) {
                        bio = user.getBio();
                    }
                    
                    // Récupérer les compétences réelles
                    if (user.getSkillTags() != null && !user.getSkillTags().isEmpty()) {
                        skills = user.getSkillTags().stream()
                                .map(skill -> skill.getName())
                                .toList();
                    }
                    
                    // Récupérer les objectifs d'apprentissage réels et remplacer complètement les objectifs par défaut
                    if (user.getLearningObjectives() != null && !user.getLearningObjectives().isEmpty()) {
                        learningObjectives = new ArrayList<>(user.getLearningObjectives());
                    }
                    
                    logger.info("Données utilisateur récupérées pour le profil: userId={}", user.getId());
                } else {
                    logger.warn("Impossible de récupérer les données de l'utilisateur avec l'ID: {}", userId);
                }
            } catch (Exception e) {
                logger.error("Erreur lors de la récupération des données utilisateur pour le profil", e);
            }
        } else {
            logger.warn("Aucun ID utilisateur en session, utilisation des données par défaut pour le profil");
        }
        
        model.addAttribute("fullName", fullName);
        model.addAttribute("email", email);
        model.addAttribute("bio", bio);
        model.addAttribute("skills", skills);
        model.addAttribute("learningObjectives", learningObjectives);
        
        return "profil";
    }
} 