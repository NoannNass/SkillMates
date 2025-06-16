package com.app.webapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePartnershipRequestDTO {
    @NotBlank(message = "L'ID du destinataire est requis")
    private String requestedId;
    
    private String message;
} 