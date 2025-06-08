package com.app.webapp.dto;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * Bean de session pour stocker les informations de l'utilisateur connecté.
 * Ce bean est créé pour la durée d'une session utilisateur.
 */
@Component
@SessionScope
public class UserInfoSession {
    private String userId;
    private String email;
    private String username;
    
    public UserInfoSession() {
        // Constructeur par défaut
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Vérifie si les informations de session sont complètes
     */
    public boolean isComplete() {
        return userId != null && email != null;
    }
    
    /**
     * Réinitialise la session
     */
    public void clear() {
        this.userId = null;
        this.email = null;
        this.username = null;
    }
} 