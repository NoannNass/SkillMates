package com.app.dto;

import java.time.LocalDate;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePartnershipGoalDTO {

    private Long partnershipId;
    
    @NotBlank(message = "Le titre est requis")
    private String title;
    
    private String description;
    
    private Integer progressPercentage;
    
    private LocalDate targetDate;
} 