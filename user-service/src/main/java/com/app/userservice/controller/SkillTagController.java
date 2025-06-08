package com.app.userservice.controller;

import com.app.userservice.dto.ApiResponse;
import com.app.userservice.dto.SkillTagDTO;
import com.app.userservice.model.SkillTag;
import com.app.userservice.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur REST pour les opérations liées aux tags de compétences.
 */
@RestController
@RequestMapping("/api/skills")
public class SkillTagController {

    private final SkillService skillService;

    @Autowired
    public SkillTagController(SkillService skillService) {
        this.skillService = skillService;
    }

    /**
     * Récupère tous les tags de compétences.
     * 
     * @return Une liste de tous les tags de compétences
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<SkillTagDTO>>> getAllSkillTags() {
        List<SkillTag> skillTags = skillService.getAllSkills();
        
        List<SkillTagDTO> skillTagDTOs = skillTags.stream()
                .map(SkillTagDTO::fromModel)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(ApiResponse.success(skillTagDTOs));
    }
} 