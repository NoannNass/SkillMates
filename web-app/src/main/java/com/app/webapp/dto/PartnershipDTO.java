package com.app.webapp.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnershipDTO {
    private Long id;
    private String requesterId;
    private String requesterName;
    private String requestedId;
    private String requestedName;
    private String status;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime endedAt;
    private List<PartnershipGoalDTO> goals;
    
    public String getPartnerName(String currentUserId) {
        return currentUserId.equals(requesterId) ? requestedName : requesterName;
    }
    
    public String getPartnerId(String currentUserId) {
        return currentUserId.equals(requesterId) ? requestedId : requesterId;
    }
} 