package com.app.userservice.dto;

import com.app.userservice.model.LearningObjective;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO pour les objectifs d'apprentissage.
 * Contient les méthodes de conversion depuis et vers le modèle.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearningObjectiveDTO {
    private String id;
    private String name;
    private String title;
    private String category;
    private String description;
    private Integer progressPercentage;
    private String userId;
    private Date createdAt;
    private Date updatedAt;
    
    /**
     * Convertit un objet du modèle LearningObjective en DTO.
     * 
     * @param model L'objet modèle à convertir
     * @return Un objet DTO correspondant
     */
    public static LearningObjectiveDTO fromModel(LearningObjective model) {
        if (model == null) return null;
        
        LearningObjectiveDTO dto = new LearningObjectiveDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setTitle(model.getTitle());
        dto.setCategory(model.getCategory());
        dto.setDescription(model.getDescription());
        dto.setProgressPercentage(model.getProgressPercentage());
        dto.setUserId(model.getUserId());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setUpdatedAt(model.getUpdatedAt());
        return dto;
    }
    
    /**
     * Convertit ce DTO en objet du modèle LearningObjective.
     * 
     * @return Un objet modèle correspondant à ce DTO
     */
    public LearningObjective toModel() {
        LearningObjective model = new LearningObjective();
        model.setId(this.id);
        model.setName(this.name);
        model.setTitle(this.title);
        model.setCategory(this.category);
        model.setDescription(this.description);
        model.setProgressPercentage(this.progressPercentage);
        model.setUserId(this.userId);
        model.setCreatedAt(this.createdAt);
        model.setUpdatedAt(this.updatedAt);
        return model;
    }
} 