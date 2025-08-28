package com.app.sessionservice.service;

import java.time.LocalDateTime;
import java.util.List;

import com.app.sessionservice.model.enums.SessionStatus;
import com.app.sessionservice.dto.CreateSessionRequest;
import com.app.sessionservice.dto.RescheduleRequest;
import com.app.sessionservice.dto.SessionResponse;
import com.app.sessionservice.dto.UpdateSessionRequest;

public interface SessionService {
    SessionResponse create(CreateSessionRequest request);
    SessionResponse getById(Long id, Long requesterId);
    List<SessionResponse> listUserSessions(Long userId, SessionStatus status, LocalDateTime from, LocalDateTime to);
    SessionResponse update(Long id, Long requesterId, UpdateSessionRequest request);
    SessionResponse reschedule(Long id, Long requesterId, RescheduleRequest request);
    SessionResponse cancel(Long id, Long requesterId);
    SessionResponse complete(Long id, Long requesterId);
}
