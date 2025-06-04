package com.app.userservice.dto;


import com.app.userservice.model.InterestTag;
import com.app.userservice.model.SkillTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {


    private String username;
    private String email;
    private String bio;
    private List<SkillTagDTO> skillTags;
    private List<LearningObjectiveDTO> learningObjectives;
    private List<InterestTagDTO> interestTags;
    private List<String> parternshipIds;
    private Date createdAt;
    private Date updatedAt;
    private String password;
    private String role;

    /**
     * Convertit un objet du modèle UserProfile en UserProfileDTO.
     * Cette méthode statique facilite la conversion dans les contrôleurs.
     *
     * @param model L'objet modèle à convertir
     * @return Un objet DTO correspondant
     */
    public static UserDto fromModel(com.app.userservice.model.User model) {
        UserDto dto = new UserDto();
        dto.setUsername(model.getUsername());
        dto.setEmail(model.getEmail());
        dto.setBio(model.getBio());
        dto.setCreatedAt(model.getCreateAt());
        dto.setUpdatedAt(model.getUpdatedAt());
        dto.setParternshipIds(model.getPartnershipIds());
        dto.setPassword(model.getPassword());
        dto.setRole(model.getRole());

        // Conversion des compétences si présentes
        if (model.getSkills() != null) {
            dto.setSkillTags(model.getSkills().stream()
                    .map(SkillTagDTO::fromModel)
                    .collect(Collectors.toList()));
        }

        // Conversion des intérêts si présents
        if (model.getInterests() != null) {
            dto.setInterestTags(model.getInterests().stream()
                    .map(InterestTagDTO::fromModel)
                    .collect(Collectors.toList()));
        }

        // Conversion des objectifs d'apprentissage si présents
        if (model.getLearningObjectives() != null) {
            dto.setLearningObjectives(model.getLearningObjectives().stream()
                    .map(LearningObjectiveDTO::fromModel)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    /**
     * Convertit ce DTO en objet du modèle UserProfile.
     * Utile lors de la réception de données depuis l'API.
     *
     * @return Un objet modèle correspondant à ce DTO
     */
    public com.app.userservice.model.User toModel() {
        com.app.userservice.model.User model = new com.app.userservice.model.User();
        model.setUsername(this.username);
        model.setEmail(this.email);
        model.setBio(this.bio);
        model.setCreateAt(this.createdAt);
        model.setUpdatedAt(this.updatedAt);
        model.setPartnershipIds(this.parternshipIds);
        model.setPassword(this.password);
        model.setRole(this.role);

        // Conversion des compétences si présentes
        if (this.skillTags != null) {
            List<SkillTag> skillTagList = this.skillTags.stream()
                    .map(SkillTagDTO::toModel)
                    .collect(Collectors.toList());
            model.setSkills(skillTagList);
        }

        // Conversion des intérêts si présents
        if (this.interestTags != null) {
            List<InterestTag> interestTagList = this.interestTags.stream()
                    .map(InterestTagDTO::toModel)
                    .collect(Collectors.toList());
            model.setInterests(interestTagList);
        }

        // Conversion des objectifs d'apprentissage si présents
        if (this.learningObjectives != null) {
            model.setLearningObjectives(this.learningObjectives.stream()
                    .map(LearningObjectiveDTO::toModel)
                    .collect(Collectors.toList()));
        }

        return model;
    }




}
