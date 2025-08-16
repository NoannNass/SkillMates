package com.app.userservice.dto;

import com.app.userservice.model.SkillTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les tags de compétences.
 * Permet de transférer les données de compétences entre l'API et le modèle.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillTagDTO {
    private String id;
    private String name;
    private String category;
    private boolean isPredefined;
    
    // Constructeur supplémentaire pour la compatibilité
    public SkillTagDTO(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.isPredefined = false; // Valeur par défaut
    }
    
    /**
     * Convertit un objet du modèle SkillTag en DTO.
     * 
     * @param model L'objet modèle à convertir
     * @return Un objet DTO correspondant
     */
    public static SkillTagDTO fromModel(SkillTag model) {
        if (model == null) return null;
        
        SkillTagDTO dto = new SkillTagDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setCategory(model.getCategory());
        dto.setPredefined(model.isPredefined());
        return dto;
    }
    
    /**
     * Convertit ce DTO en objet du modèle SkillTag.
     * 
     * @return Un objet modèle correspondant à ce DTO
     */
    public SkillTag toModel() {
        SkillTag model = new SkillTag();
        model.setId(this.id);
        model.setName(this.name);
        model.setCategory(this.category);
        model.setPredefined(this.isPredefined);
        return model;
    }
} 