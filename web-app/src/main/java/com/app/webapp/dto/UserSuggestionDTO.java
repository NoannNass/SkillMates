package com.app.webapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSuggestionDTO {
    private String userId;
    private String username;
    private String bio;
    private String avatarUrl;
    private int commonInterests;
} 