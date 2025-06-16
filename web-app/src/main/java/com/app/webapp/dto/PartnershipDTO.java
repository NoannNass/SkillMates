package com.app.webapp.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class PartnershipDTO {
    private Long id;
    private String requesterId;
    private String requestedId;
    private String status;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime endedAt;
    private List<PartnershipGoalDTO> goals;
} 