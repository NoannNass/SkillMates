package com.app.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;
import java.util.Objects;

@Document(collection = "learning_objectives")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearningObjective {

    @Id
    private String id;


    private String title;


    private String description;

    // Pourcentage de progression
    private Integer progressPercentage = 0;


    // Référence à l'utilisateur propriétaire de cet objectif
    private String userId;

    private Date createdAt;

    private Date updatedAt;

    // equals, hashCode et toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LearningObjective that = (LearningObjective) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "LearningObjective{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", progressPercentage=" + progressPercentage +
                ", userId='" + userId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}