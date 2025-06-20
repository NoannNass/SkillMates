package com.app.userservice.dto;

import com.app.userservice.model.InterestTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les tags d'intérêts.
 * Permet de transférer les données d'intérêts entre l'API et le modèle.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterestTagDTO {
    private String id;
    private String name;
    private String category;
    private boolean isPredefined;
    
    // Constructeur supplémentaire pour la compatibilité
    public InterestTagDTO(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.isPredefined = false; // Valeur par défaut
    }
    
    /**
     * Convertit un objet du modèle InterestTag en DTO.
     * 
     * @param model L'objet modèle à convertir
     * @return Un objet DTO correspondant
     */
    public static InterestTagDTO fromModel(InterestTag model) {
        if (model == null) return null;
        
        InterestTagDTO dto = new InterestTagDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setCategory(model.getCategory());
        dto.setPredefined(model.isPredefined());
        return dto;
    }
    
    /**
     * Convertit ce DTO en objet du modèle InterestTag.
     * 
     * @return Un objet modèle correspondant à ce DTO
     */
    public InterestTag toModel() {
        InterestTag model = new InterestTag();
        model.setId(this.id);
        model.setName(this.name);
        model.setCategory(this.category);
        model.setPredefined(this.isPredefined);
        return model;
    }
} 