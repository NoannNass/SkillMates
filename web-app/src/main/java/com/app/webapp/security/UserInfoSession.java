package com.app.webapp.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@SessionScope
public class UserInfoSession {
    private String userId;
    private String username;
    private String email;
    private String role;
} 