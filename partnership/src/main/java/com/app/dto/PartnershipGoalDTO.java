package com.app.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PartnershipGoalDTO {
    private Long id;
    private String title;
    private String description;
    private int progressPercentage;
    private LocalDate targetDate;
} 