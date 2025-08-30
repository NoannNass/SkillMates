package com.app.webapp.dto.session;

import java.time.LocalDateTime;

public class UpdateSessionRequest {
    private String title;
    private String description;
    private SessionType type;
    private String locationOrLink;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public SessionType getType() { return type; }
    public void setType(SessionType type) { this.type = type; }
    public String getLocationOrLink() { return locationOrLink; }
    public void setLocationOrLink(String locationOrLink) { this.locationOrLink = locationOrLink; }
    public LocalDateTime getStartAt() { return startAt; }
    public void setStartAt(LocalDateTime startAt) { this.startAt = startAt; }
    public LocalDateTime getEndAt() { return endAt; }
    public void setEndAt(LocalDateTime endAt) { this.endAt = endAt; }
}


