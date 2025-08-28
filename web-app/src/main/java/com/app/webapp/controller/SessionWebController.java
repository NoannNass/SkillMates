package com.app.webapp.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.webapp.client.PartnershipClient;
import com.app.webapp.client.SessionClient;
import com.app.webapp.dto.ApiResponse;
import com.app.webapp.dto.PartnershipDTO;
import com.app.webapp.dto.session.CreateSessionRequest;
import com.app.webapp.dto.session.SessionResponse;
import com.app.webapp.dto.session.SessionStatus;
import com.app.webapp.security.UserInfoSession;

@Controller
@RequestMapping("/sessions")
public class SessionWebController {

    private final SessionClient sessionClient;
    private final UserInfoSession userInfoSession;
    private final PartnershipClient partnershipClient;

    public SessionWebController(SessionClient sessionClient, UserInfoSession userInfoSession, PartnershipClient partnershipClient) {
        this.sessionClient = sessionClient;
        this.userInfoSession = userInfoSession;
        this.partnershipClient = partnershipClient;
    }

    @GetMapping
    public String list(@RequestParam(value = "status", required = false) SessionStatus status,
                       @RequestParam(value = "from", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                       @RequestParam(value = "to", required = false)
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
                       Model model) {
        String userIdStr = userInfoSession.getUserId();
        if (userIdStr == null || userIdStr.isBlank()) {
            return "redirect:/login";
        }
        String userId = userIdStr;
        try {
            List<SessionResponse> sessions = sessionClient.list(userId, status, from, to);
            model.addAttribute("sessions", sessions);
        } catch (Exception ex) {
            model.addAttribute("sessions", java.util.List.of());
            model.addAttribute("error", "Impossible de charger les sessions pour le moment.");
        }
        model.addAttribute("status", status);
        return "sessions/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        CreateSessionRequest form = new CreateSessionRequest();
        model.addAttribute("form", form);
        String userId = userInfoSession.getUserId();
        model.addAttribute("currentUserId", userId);
        try {
            ApiResponse<java.util.List<PartnershipDTO>> resp = partnershipClient.getActivePartnerships(userId);
            java.util.List<PartnershipDTO> partnerships = resp != null ? resp.getData() : java.util.List.of();
            model.addAttribute("activePartnerships", partnerships);
        } catch (Exception e) {
            model.addAttribute("activePartnerships", java.util.List.of());
        }
        return "sessions/create";
    }

    @PostMapping
    public String create(@ModelAttribute("form") CreateSessionRequest form, BindingResult br, Model model) {
        String userId = userInfoSession.getUserId();
        form.setOrganizerId(userId);
        if (form.getPartnerId() == null || form.getPartnerId().isBlank()) {
            br.reject("partner.required", "Le partenaire est requis");
        }
        // Résoudre partnershipId depuis la sélection
        if (!br.hasErrors()) {
            try {
                ApiResponse<java.util.List<PartnershipDTO>> resp = partnershipClient.getActivePartnerships(userId);
                java.util.List<PartnershipDTO> partnerships = resp != null ? resp.getData() : java.util.List.of();
                java.util.Optional<Long> partnershipId = partnerships.stream()
                        .filter(p -> (userId.equals(p.getRequesterId()) && form.getPartnerId().equals(p.getRequestedId()))
                                  || (userId.equals(p.getRequestedId()) && form.getPartnerId().equals(p.getRequesterId())))
                        .map(PartnershipDTO::getId)
                        .findFirst();
                if (partnershipId.isEmpty()) {
                    br.reject("partner.invalid", "Aucun partenariat actif trouvé avec ce partenaire");
                } else {
                    form.setPartnershipId(partnershipId.get());
                }
            } catch (Exception e) {
                br.reject("partner.lookup.error", "Erreur lors du chargement des partenariats actifs");
            }
        }
        if (br.hasErrors()) {
            return "sessions/create";
        }
        sessionClient.create(form);
        return "redirect:/sessions";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        String userId = userInfoSession.getUserId();
        SessionResponse session = sessionClient.getById(id, userId);
        model.addAttribute("session", session);
        return "sessions/details";
    }
}
