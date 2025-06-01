package com.app.userservice.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor

public class User {


    @Id
    private String id;

    private String username;

    private String email;

    private String bio;

    private Date createAt;

    private Date updatedAt;

    private ProfileCompletionStatus completionStatus = ProfileCompletionStatus.INITIAL;


    @DBRef
    private List<SkillTag> skills = new ArrayList<>();

    @DBRef
    private List<LearningObjective> learningObjectives = new ArrayList<>();

    @DBRef
    private List<InterestTag> interests = new ArrayList<>();

    
    private List<String> partnershipIds = new ArrayList<>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ProfileCompletionStatus getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(ProfileCompletionStatus completionStatus) {
        this.completionStatus = completionStatus;
    }

    public List<SkillTag> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillTag> skills) {
        this.skills = skills;
    }

    public List<LearningObjective> getLearningObjectives() {
        return learningObjectives;
    }

    public void setLearningObjectives(List<LearningObjective> learningObjectives) {
        this.learningObjectives = learningObjectives;
    }

    public List<InterestTag> getInterests() {
        return interests;
    }

    public void setInterests(List<InterestTag> interests) {
        this.interests = interests;
    }

    public List<String> getPartnershipIds() {
        return partnershipIds;
    }

    public void setPartnershipIds(List<String> partnershipIds) {
        this.partnershipIds = partnershipIds;
    }
}
