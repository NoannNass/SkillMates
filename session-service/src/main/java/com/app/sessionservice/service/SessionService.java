package com.app.sessionservice.service;

import java.time.LocalDateTime;
import java.util.List;

import com.app.sessionservice.dto.CreateSessionRequest;
import com.app.sessionservice.dto.RescheduleRequest;
import com.app.sessionservice.dto.SessionResponse;
import com.app.sessionservice.dto.UpdateSessionRequest;
import com.app.sessionservice.model.enums.SessionStatus;

public interface SessionService {
    SessionResponse create(CreateSessionRequest request);
    SessionResponse getById(Long id, String requesterId);
    List<SessionResponse> listUserSessions(String userId, SessionStatus status, LocalDateTime from, LocalDateTime to);
    SessionResponse update(Long id, String requesterId, UpdateSessionRequest request);
    SessionResponse reschedule(Long id, String requesterId, RescheduleRequest request);
    SessionResponse cancel(Long id, String requesterId);
    SessionResponse complete(Long id, String requesterId);
}
