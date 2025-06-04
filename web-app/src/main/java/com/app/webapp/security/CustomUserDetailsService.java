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

    private UserClient userClient;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // Recherche de l'utilisateur via le client Feign
            ApiResponse<UserDto> response = userClient.getUserByUsername(username);
            if (response == null || response.getData() == null) {
                throw new UsernameNotFoundException("Utilisateur non trouvé: " + username);
            }
            UserDto userDTO = response.getData();
            // Conversion du rôle au format Spring Security (ROLE_XXX)
            String role = userDTO.getRole() != null ? "ROLE_" + userDTO.getRole().toUpperCase() : "ROLE_USER";
            // Retourne l'objet UserDetails de Spring Security
            return new User(
                    userDTO.getEmail(),
                    userDTO.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(role))
            );
        } catch (Exception e) {
            throw new UsernameNotFoundException("Erreur lors de la recherche de l'utilisateur: " + e.getMessage());
        }
    }
}
