package com.app.webapp.dto;

public class CreatePartnershipRequestDTO {

    private String requesterId;
    private String requestedId;
    private String message;

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequestedId() {
        return requestedId;
    }

    public void setRequestedId(String requestedId) {
        this.requestedId = requestedId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}