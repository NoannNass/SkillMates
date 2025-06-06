package com.app.webapp.security;

import java.util.Collections;

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

    private final UserClient userClient;

    @Autowired
    public CustomUserDetailsService(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            System.out.println("Tentative de login pour l'email : " + email);
            // Recherche de l'utilisateur via le client Feign par email
            ApiResponse<UserDto> response = userClient.getUserByEmail(email);
            if (response == null || response.getData() == null) {
                System.out.println("Aucun utilisateur trouvé pour l'email : " + email);
                throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email);
            }
            UserDto userDTO = response.getData();
            System.out.println("Utilisateur trouvé : " + userDTO.getEmail() + " / password hash : " + userDTO.getPassword());
            // Conversion du rôle au format Spring Security (ROLE_XXX)
            String role = userDTO.getRole() != null ? "ROLE_" + userDTO.getRole().toUpperCase() : "ROLE_USER";
            // Retourne l'objet UserDetails de Spring Security
            return new User(
                    userDTO.getEmail(),
                    userDTO.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(role))
            );
        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche de l'utilisateur : " + e.getMessage());
            throw new UsernameNotFoundException("Erreur lors de la recherche de l'utilisateur: " + e.getMessage());
        }
    }
}
