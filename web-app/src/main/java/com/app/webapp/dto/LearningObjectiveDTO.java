package com.app.webapp.dto;


public class LearningObjectiveDTO {
    private String id;
    private String title;
    private String description;
    private int progressPercentage;


    public LearningObjectiveDTO() {
    }

    public LearningObjectiveDTO(String id, String title, String description, int progressPercentage) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.progressPercentage = progressPercentage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
}