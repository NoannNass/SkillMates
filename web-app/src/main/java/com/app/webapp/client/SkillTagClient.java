package com.app.webapp.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.webapp.dto.ApiResponse;
import com.app.webapp.dto.SkillTagDTO;

@FeignClient(name = "user-service", path = "/api/skills")
public interface SkillTagClient {
    @GetMapping
    ApiResponse<List<SkillTagDTO>> getAllSkillTags();
} 