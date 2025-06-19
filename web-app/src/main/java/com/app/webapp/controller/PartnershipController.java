package com.app.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.webapp.client.PartnershipClient;
import com.app.webapp.security.UserInfoSession;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/partnerships")
public class PartnershipController {

    private final PartnershipClient partnershipClient;
    private final UserInfoSession userInfoSession;

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
        String userId = userInfoSession.getUserId();
        var suggestions = partnershipClient.getPartnershipSuggestions(userId);
        
        model.addAttribute("suggestions", suggestions.getData());
        model.addAttribute("username", userInfoSession.getUsername());
        
        return "suggestions";
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
} 