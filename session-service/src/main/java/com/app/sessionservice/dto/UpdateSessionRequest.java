package com.app.sessionservice.dto;

import java.time.LocalDateTime;

import com.app.sessionservice.model.enums.SessionType;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateSessionRequest {

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 4000)
    private String description;

    @NotNull
    private SessionType type;

    @Size(max = 512)
    private String locationOrLink;

    @NotNull
    @Future
    private LocalDateTime startAt;

    @NotNull
    private LocalDateTime endAt;

    @AssertTrue(message = "startAt must be before endAt")
    public boolean isStartBeforeEnd() {
        if (startAt == null || endAt == null) {
            return true;
        }
        return startAt.isBefore(endAt);
    }

    @AssertTrue(message = "locationOrLink is required for VIRTUAL or PHYSICAL accordingly")
    public boolean isLocationOrLinkValidForType() {
        if (type == null) {
            return true;
        }
        if (type == SessionType.VIRTUAL) {
            return locationOrLink != null && !locationOrLink.isBlank();
        }
        if (type == SessionType.PHYSICAL) {
            return locationOrLink != null && !locationOrLink.isBlank();
        }
        return true;
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

    public SessionType getType() {
        return type;
    }

    public void setType(SessionType type) {
        this.type = type;
    }

    public String getLocationOrLink() {
        return locationOrLink;
    }

    public void setLocationOrLink(String locationOrLink) {
        this.locationOrLink = locationOrLink;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }
}


