package com.app.webapp.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.webapp.client.PartnershipClient;
import com.app.webapp.client.UserClient;
import com.app.webapp.dto.ApiResponse;
import com.app.webapp.dto.CreatePartnershipRequestDTO;
import com.app.webapp.dto.UserDto;
import com.app.webapp.security.UserInfoSession;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/partnerships")
public class PartnershipController {

    private final PartnershipClient partnershipClient;
    private final UserInfoSession userInfoSession;
    private final UserClient userClient;

    @GetMapping
    public String partnershipsPage(Model model) {
        String userId = userInfoSession.getUserId();
        
        // Récupérer les partenariats en attente et actifs
        var pendingPartnerships = partnershipClient.getPendingPartnerships(userId);
        var activePartnerships = partnershipClient.getActivePartnerships(userId);
        
        // Ajouter les données au modèle
        model.addAttribute("pendingPartnerships", pendingPartnerships.getData());
        model.addAttribute("activePartnerships", activePartnerships.getData());
        model.addAttribute("username", userInfoSession.getUsername());
        
        return "partnerships";
    }

    @GetMapping("/suggestions")
    public String suggestionsPage(Model model) {
        model.addAttribute("username", userInfoSession.getUsername());
        return "suggestions";
    }

    @PostMapping("/search-by-username")
    public String searchByUsername(@RequestParam String username, Model model) {
        var user = partnershipClient.searchUserByUsername(username);
        model.addAttribute("searchedUser", user.getData());
        model.addAttribute("username", userInfoSession.getUsername());
        return "suggestions";
    }

    @PostMapping("/request")
    @ResponseBody
    public ApiResponse<?> createPartnershipRequest(@RequestBody CreatePartnershipRequestDTO request) {
        request.setRequesterId(userInfoSession.getUserId());
        return partnershipClient.createPartnershipRequest(request);
    }

    @PostMapping("/{partnershipId}/accept")
    public String acceptPartnership(@PathVariable Long partnershipId) {
        partnershipClient.acceptPartnership(partnershipId);
        return "redirect:/partnerships";
    }

    @PostMapping("/{partnershipId}/deny")
    public String denyPartnership(@PathVariable Long partnershipId) {
        partnershipClient.denyPartnership(partnershipId);
        return "redirect:/partnerships";
    }

    @GetMapping("/api/partnerships/search")
    @ResponseBody
    public ApiResponse<List<UserDto>> searchUsers(@RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            // Retourner tous les utilisateurs si la requête est vide
            return userClient.getAllUsers();
        } else {
            // Rechercher par nom d'utilisateur
            return userClient.searchUsers(query);
        }
    }
} 