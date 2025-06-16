package com.app.dto;

import lombok.Data;

@Data
public class UserSuggestionDTO {
    private String userId;
    private String username;
    private String email;
    private double matchScore;
    private String[] commonInterests;
} 