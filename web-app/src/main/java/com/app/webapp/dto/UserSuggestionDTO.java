package com.app.webapp.dto;

import lombok.Data;

@Data
public class UserSuggestionDTO {
    private String userId;
    private String username;
    private String email;
    private String bio;
    private double matchScore;
} 