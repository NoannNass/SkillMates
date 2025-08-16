package com.app.webapp.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.app.webapp.dto.ApiResponse;
import com.app.webapp.dto.CreatePartnershipRequestDTO;
import com.app.webapp.dto.PartnershipDTO;
import com.app.webapp.dto.UserDto;
import com.app.webapp.dto.UserSuggestionDTO;

@FeignClient(name = "partnership-service", path = "/api/partnerships")
public interface PartnershipClient {
    
    @PostMapping("/request")
    ApiResponse<PartnershipDTO> createPartnershipRequest(@RequestBody CreatePartnershipRequestDTO request);
    
    @GetMapping("/user/{userId}")
    ApiResponse<List<PartnershipDTO>> getUserPartnerships(@PathVariable String userId);
    
    @GetMapping("/pending/{userId}")
    ApiResponse<List<PartnershipDTO>> getPendingPartnerships(@PathVariable String userId);
    
    @GetMapping("/active/{userId}")
    ApiResponse<List<PartnershipDTO>> getActivePartnerships(@PathVariable String userId);
    
    @PutMapping("/{partnershipId}/accept")
    ApiResponse<PartnershipDTO> acceptPartnership(@PathVariable Long partnershipId);
    
    @PutMapping("/{partnershipId}/deny")
    ApiResponse<PartnershipDTO> denyPartnership(@PathVariable Long partnershipId);
    
    @PutMapping("/{partnershipId}/cancel")
    ApiResponse<PartnershipDTO> cancelPartnership(@PathVariable Long partnershipId);
    
    @PutMapping("/{partnershipId}/end")
    ApiResponse<PartnershipDTO> endPartnership(@PathVariable Long partnershipId);
    
    @GetMapping("/suggestions/{userId}")
    ApiResponse<List<UserSuggestionDTO>> getPartnershipSuggestions(@PathVariable String userId);
    
    @GetMapping("/{partnershipId}")
    ApiResponse<PartnershipDTO> getPartnership(@PathVariable Long partnershipId);
    
    @GetMapping("/search/username/{username}")
    ApiResponse<UserDto> searchUserByUsername(@PathVariable String username);
} 