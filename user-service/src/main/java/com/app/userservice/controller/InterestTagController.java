package com.app.userservice.controller;

import com.app.userservice.dto.ApiResponse;
import com.app.userservice.dto.InterestTagDTO;
import com.app.userservice.model.InterestTag;
import com.app.userservice.repository.InterestTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/interests")
public class InterestTagController {

    private final InterestTagRepository interestTagRepository;

    @Autowired
    public InterestTagController(InterestTagRepository interestTagRepository) {
        this.interestTagRepository = interestTagRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InterestTagDTO>>> getAllInterestTags() {
        List<InterestTag> tags = interestTagRepository.findAll();
        List<InterestTagDTO> tagDTOs = tags.stream()
                .map(tag -> new InterestTagDTO(tag.getId(), tag.getName(), tag.getCategory()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(tagDTOs));
    }
} 