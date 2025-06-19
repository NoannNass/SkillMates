package com.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.ApiResponse;
import com.app.dto.CreatePartnershipRequestDTO;
import com.app.dto.PartnershipDTO;
import com.app.dto.UserSuggestionDTO;
import com.app.service.PartnershipService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/partnerships")
@RequiredArgsConstructor
public class PartnershipController {

    private final PartnershipService partnershipService;

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<PartnershipDTO>> createPartnershipRequest(
            @Valid @RequestBody CreatePartnershipRequestDTO request) {
        return ResponseEntity.ok(partnershipService.createPartnershipRequest(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<PartnershipDTO>>> getUserPartnerships(
            @PathVariable String userId) {
        return ResponseEntity.ok(partnershipService.getUserPartnerships(userId));
    }

    @PutMapping("/{partnershipId}/accept")
    public ResponseEntity<ApiResponse<PartnershipDTO>> acceptPartnership(
            @PathVariable Long partnershipId) {
        return ResponseEntity.ok(partnershipService.acceptPartnership(partnershipId));
    }

    @PutMapping("/{partnershipId}/deny")
    public ResponseEntity<ApiResponse<PartnershipDTO>> denyPartnership(
            @PathVariable Long partnershipId) {
        return ResponseEntity.ok(partnershipService.denyPartnership(partnershipId));
    }

    @PutMapping("/{partnershipId}/cancel")
    public ResponseEntity<ApiResponse<PartnershipDTO>> cancelPartnership(
            @PathVariable Long partnershipId) {
        return ResponseEntity.ok(partnershipService.cancelPartnership(partnershipId));
    }

    @PutMapping("/{partnershipId}/end")
    public ResponseEntity<ApiResponse<PartnershipDTO>> endPartnership(
            @PathVariable Long partnershipId) {
        return ResponseEntity.ok(partnershipService.endPartnership(partnershipId));
    }

    @GetMapping("/suggestions/{userId}")
    public ResponseEntity<ApiResponse<List<UserSuggestionDTO>>> getPartnershipSuggestions(
            @PathVariable String userId) {
        return ResponseEntity.ok(partnershipService.getPartnershipSuggestions(userId));
    }

    @GetMapping("/{partnershipId}")
    public ResponseEntity<ApiResponse<PartnershipDTO>> getPartnership(
            @PathVariable Long partnershipId) {
        return ResponseEntity.ok(partnershipService.getPartnership(partnershipId));
    }

    @GetMapping("/pending/{userId}")
    public ResponseEntity<ApiResponse<List<PartnershipDTO>>> getPendingPartnerships(@PathVariable String userId) {
        List<PartnershipDTO> all = partnershipService.getUserPartnerships(userId).getData();
        List<PartnershipDTO> pending = all.stream()
            .filter(p -> p.getStatus() != null && p.getStatus().name().equals("PENDING"))
            .toList();
        return ResponseEntity.ok(new ApiResponse<>(true, "Partenariats en attente récupérés", pending));
    }

    @GetMapping("/active/{userId}")
    public ResponseEntity<ApiResponse<List<PartnershipDTO>>> getActivePartnerships(@PathVariable String userId) {
        List<PartnershipDTO> all = partnershipService.getUserPartnerships(userId).getData();
        List<PartnershipDTO> active = all.stream()
            .filter(p -> p.getStatus() != null && p.getStatus().name().equals("ACCEPTED"))
            .toList();
        return ResponseEntity.ok(new ApiResponse<>(true, "Partenariats actifs récupérés", active));
    }
} 