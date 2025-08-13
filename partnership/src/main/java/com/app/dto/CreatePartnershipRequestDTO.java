package com.app.dto;

import lombok.Data;

@Data
public class CreatePartnershipRequestDTO {
    private String requesterId;

    private String requestedId;
    
    private String message;
} 