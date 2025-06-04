package com.app.webapp.dto;

import java.util.Date;
import java.util.List;


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


    public UserDto(String username) {
        this.username = username;
    }

    public UserDto(String username, String email, String bio, List<SkillTagDTO> skillTags, List<LearningObjectiveDTO> learningObjectives, List<InterestTagDTO> interestTags, List<String> parternshipIds, Date createdAt, Date updatedAt, String password, String role) {
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.skillTags = skillTags;
        this.learningObjectives = learningObjectives;
        this.interestTags = interestTags;
        this.parternshipIds = parternshipIds;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.password = password;
        this.role = role;
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

    public List<SkillTagDTO> getSkillTags() {
        return skillTags;
    }

    public void setSkillTags(List<SkillTagDTO> skillTags) {
        this.skillTags = skillTags;
    }

    public List<LearningObjectiveDTO> getLearningObjectives() {
        return learningObjectives;
    }

    public void setLearningObjectives(List<LearningObjectiveDTO> learningObjectives) {
        this.learningObjectives = learningObjectives;
    }

    public List<InterestTagDTO> getInterestTags() {
        return interestTags;
    }

    public void setInterestTags(List<InterestTagDTO> interestTags) {
        this.interestTags = interestTags;
    }

    public List<String> getParternshipIds() {
        return parternshipIds;
    }

    public void setParternshipIds(List<String> parternshipIds) {
        this.parternshipIds = parternshipIds;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}