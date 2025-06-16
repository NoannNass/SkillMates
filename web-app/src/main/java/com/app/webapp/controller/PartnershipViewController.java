package com.app.webapp.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.webapp.client.PartnershipClient;
import com.app.webapp.dto.ApiResponse;
import com.app.webapp.dto.PartnershipDTO;
import com.app.webapp.dto.UserInfoSession;
import com.app.webapp.dto.UserSuggestionDTO;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/partnerships")
@RequiredArgsConstructor
public class PartnershipViewController {

    private final PartnershipClient partnershipClient;
    private final UserInfoSession userInfoSession;

    @GetMapping
    public String partnershipsPage(Model model) {
        String userId = userInfoSession.getUserId();
        ApiResponse<List<PartnershipDTO>> partnerships = partnershipClient.getUserPartnerships(userId);
        model.addAttribute("partnerships", partnerships.getData());
        return "partnerships/list";
    }

    @GetMapping("/suggestions")
    public String suggestionsPage(Model model) {
        String userId = userInfoSession.getUserId();
        ApiResponse<List<UserSuggestionDTO>> suggestions = partnershipClient.getPartnershipSuggestions(userId);
        model.addAttribute("suggestions", suggestions.getData());
        return "partnerships/suggestions";
    }

    @GetMapping("/{id}")
    public String partnershipDetails(@PathVariable Long id, Model model) {
        ApiResponse<PartnershipDTO> partnership = partnershipClient.getPartnership(id);
        model.addAttribute("partnership", partnership.getData());
        return "partnerships/details";
    }

    @PostMapping("/{id}/accept")
    public String acceptPartnership(@PathVariable Long id) {
        partnershipClient.acceptPartnership(id);
        return "redirect:/partnerships/" + id;
    }

    @PostMapping("/{id}/deny")
    public String denyPartnership(@PathVariable Long id) {
        partnershipClient.denyPartnership(id);
        return "redirect:/partnerships";
    }

    @PostMapping("/{id}/cancel")
    public String cancelPartnership(@PathVariable Long id) {
        partnershipClient.cancelPartnership(id);
        return "redirect:/partnerships";
    }

    @PostMapping("/{id}/end")
    public String endPartnership(@PathVariable Long id) {
        partnershipClient.endPartnership(id);
        return "redirect:/partnerships";
    }
} 