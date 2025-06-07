package com.app.webapp.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.webapp.dto.ApiResponse;
import com.app.webapp.dto.InterestTagDTO;

@FeignClient(name = "user-service", path = "/api/interests")
public interface InterestTagClient {
    @GetMapping
    ApiResponse<List<InterestTagDTO>> getAllInterestTags();
} 