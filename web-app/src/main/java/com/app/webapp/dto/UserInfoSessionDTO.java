package com.app.webapp.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Bean de session pour stocker les informations de l'utilisateur connecté.
 * Ce bean est créé pour la durée d'une session utilisateur.
 */
@Getter
@Setter
public class UserInfoSessionDTO {
    private String userId;
    private String username;
    private String email;
    private String role;
    
    /**
     * Vérifie si les informations de session sont complètes
     */
    public boolean isComplete() {
        boolean complete = userId != null && email != null;
        return complete;
    }
    
    /**
     * Réinitialise la session
     */
    public void clear() {
        this.userId = null;
        this.email = null;
        this.username = null;
    }
    
    @Override
    public String toString() {
        return "UserInfoSessionDTO [userId=" + userId + ", email=" + email + ", username=" + username + "]";
    }
} 