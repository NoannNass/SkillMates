package com.app.webapp.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.app.webapp.dto.PartnershipDTO;
import com.app.webapp.dto.UserDto;
import com.app.webapp.security.UserInfoSession;

 

@Controller
@RequestMapping("/partnerships")
public class PartnershipController {

    private final PartnershipClient partnershipClient;
    private final UserInfoSession userInfoSession;
    private final UserClient userClient;

    @Autowired
    public PartnershipController(
            PartnershipClient partnershipClient,
            UserInfoSession userInfoSession,
            UserClient userClient) {
        this.partnershipClient = partnershipClient;
        this.userInfoSession = userInfoSession;
        this.userClient = userClient;
    }

    @GetMapping
    public String partnershipsPage(Model model) {
        String userId = userInfoSession.getUserId();

        var pendingPartnerships = partnershipClient.getPendingPartnerships(userId);
        var activePartnerships = partnershipClient.getActivePartnerships(userId);

        // Enrichir avec le nom du partenaire attendu par le template (partnerName)
        enrichPartnerNames(pendingPartnerships.getData(), userId);
        enrichPartnerNames(activePartnerships.getData(), userId);

        model.addAttribute("pendingPartnerships", pendingPartnerships.getData());
        model.addAttribute("activePartnerships", activePartnerships.getData());
        model.addAttribute("username", userInfoSession.getUsername());
        model.addAttribute("currentUserId", userId);

        return "partnerships";
    }

    private void enrichPartnerNames(List<PartnershipDTO> partnerships, String currentUserId) {
        if (partnerships == null) return;
        for (PartnershipDTO p : partnerships) {
            try {
                String partnerId = currentUserId != null && currentUserId.equals(p.getRequesterId())
                        ? p.getRequestedId()
                        : p.getRequesterId();
                if (partnerId == null) continue;
                var userResp = userClient.getUserById(partnerId);
                if (userResp != null && userResp.isSuccess() && userResp.getData() != null) {
                    p.setPartnerName(userResp.getData().getUsername());
                }
            } catch (Exception ignored) {
                // On ignore et laisse partnerName null si l'appel échoue
            }
        }
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
        try {
            String currentUserId = userInfoSession.getUserId();
            if (currentUserId == null || currentUserId.isBlank()) {
                return ApiResponse.error("Utilisateur non authentifié");
            }

            request.setRequesterId(currentUserId);
            return partnershipClient.createPartnershipRequest(request);
        } catch (Exception ex) {
            // Toujours renvoyer du JSON vers le front afin d'éviter une page HTML d'erreur
            return ApiResponse.error("Impossible d'envoyer la demande pour le moment. Veuillez réessayer.");
        }
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
        String currentUserId = userInfoSession.getUserId();

        ApiResponse<List<UserDto>> response =
                (query == null || query.trim().isEmpty())
                        ? userClient.getAllUsers()
                        : userClient.searchUsers(query);

        List<UserDto> filtered = response.getData() == null
                ? List.of()
                : response.getData().stream()
                        .filter(u -> u.getId() != null && !u.getId().equals(currentUserId))
                        .collect(Collectors.toList());

        return ApiResponse.success(filtered);
    }
} 