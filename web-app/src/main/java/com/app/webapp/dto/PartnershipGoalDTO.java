package com.app.webapp.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnershipGoalDTO {
    private Long id;
    private Long partnershipId;
    private String title;
    private String description;
    private int progressPercentage;
    private LocalDate targetDate;
} 