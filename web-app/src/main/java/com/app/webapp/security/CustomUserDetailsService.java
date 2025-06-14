package com.app.webapp.security;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.webapp.client.UserClient;
import com.app.webapp.dto.ApiResponse;
import com.app.webapp.dto.UserDto;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserClient userClient;

    @Autowired
    public CustomUserDetailsService(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            logger.info("Chargement des détails utilisateur pour l'email : {}", email);
            
            // Recherche de l'utilisateur via le client Feign par email
            ApiResponse<UserDto> response = userClient.getUserByEmail(email);
            if (response == null || response.getData() == null) {
                logger.warn("Aucun utilisateur trouvé pour l'email : {}", email);
                throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email);
            }
            UserDto userDTO = response.getData();
            logger.info("Utilisateur trouvé : {} (id: {})", userDTO.getEmail(), userDTO.getId());
            
            // Conversion du rôle au format Spring Security (ROLE_XXX)
            String role = userDTO.getRole() != null ? "ROLE_" + userDTO.getRole().toUpperCase() : "ROLE_USER";
            
            // Retourne l'objet UserDetails de Spring Security
            return new User(
                    userDTO.getEmail(),
                    userDTO.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(role))
            );
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de l'utilisateur : {}", e.getMessage(), e);
            throw new UsernameNotFoundException("Erreur lors de la recherche de l'utilisateur: " + e.getMessage());
        }
    }
}
