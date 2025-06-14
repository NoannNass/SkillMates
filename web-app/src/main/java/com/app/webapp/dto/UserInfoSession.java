package com.app.webapp.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * Bean de session pour stocker les informations de l'utilisateur connecté.
 * Ce bean est créé pour la durée d'une session utilisateur.
 */
@Component
@SessionScope
public class UserInfoSession {
    private static final Logger logger = LoggerFactory.getLogger(UserInfoSession.class);
    
    private String userId;
    private String email;
    private String username;
    
    public UserInfoSession() {
        logger.debug("Nouvelle instance de UserInfoSession créée");
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        logger.debug("UserId mis à jour: {}", userId);
        this.userId = userId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        logger.debug("Email mis à jour: {}", email);
        this.email = email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        logger.debug("Username mis à jour: {}", username);
        this.username = username;
    }
    
    /**
     * Vérifie si les informations de session sont complètes
     */
    public boolean isComplete() {
        boolean complete = userId != null && email != null;
        logger.debug("Vérification si la session est complète: {}", complete);
        return complete;
    }
    
    /**
     * Réinitialise la session
     */
    public void clear() {
        logger.debug("Réinitialisation de la session utilisateur: userId={}, email={}, username={}", 
                    userId, email, username);
        this.userId = null;
        this.email = null;
        this.username = null;
    }
    
    @Override
    public String toString() {
        return "UserInfoSession [userId=" + userId + ", email=" + email + ", username=" + username + "]";
    }
} 