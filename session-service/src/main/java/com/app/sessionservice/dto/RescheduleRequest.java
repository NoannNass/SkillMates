package com.app.sessionservice.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public class RescheduleRequest {

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


