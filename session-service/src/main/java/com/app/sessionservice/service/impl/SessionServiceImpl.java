package com.app.sessionservice.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.sessionservice.client.PartnershipClient;
import com.app.sessionservice.client.dto.ApiResponse;
import com.app.sessionservice.client.dto.PartnershipDTO;
import com.app.sessionservice.dto.CreateSessionRequest;
import com.app.sessionservice.dto.RescheduleRequest;
import com.app.sessionservice.dto.SessionResponse;
import com.app.sessionservice.dto.UpdateSessionRequest;
import com.app.sessionservice.model.Session;
import com.app.sessionservice.model.enums.SessionStatus;
import com.app.sessionservice.repository.SessionRepository;
import com.app.sessionservice.service.SessionService;

@Service
@Transactional
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final PartnershipClient partnershipClient;

    public SessionServiceImpl(SessionRepository sessionRepository, PartnershipClient partnershipClient) {
        this.sessionRepository = sessionRepository;
        this.partnershipClient = partnershipClient;
    }

    @Override
    public SessionResponse create(CreateSessionRequest request) {
        if (request.getOrganizerId() != null && request.getOrganizerId().equals(request.getPartnerId())) {
            throw new IllegalStateException("organizerId and partnerId must be different");
        }
        // Validate partnership exists and is ACCEPTED, and members match organizer/partner
        ApiResponse<PartnershipDTO> resp = partnershipClient.getPartnership(request.getPartnershipId());
        PartnershipDTO p = resp != null ? resp.getData() : null;
        if (p == null || p.getStatus() == null || !"ACCEPTED".equals(p.getStatus())) {
            throw new IllegalStateException("Partnership must be ACCEPTED");
        }
        String organizerStr = request.getOrganizerId();
        String partnerStr = request.getPartnerId();
        boolean matches = (organizerStr.equals(p.getRequesterId()) && partnerStr.equals(p.getRequestedId()))
                || (organizerStr.equals(p.getRequestedId()) && partnerStr.equals(p.getRequesterId()));
        if (!matches) {
            throw new IllegalStateException("Organizer/Partner do not match partnership members");
        }
        Session session = new Session();
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setType(request.getType());
        session.setLocationOrLink(request.getLocationOrLink());
        session.setStartAt(request.getStartAt());
        session.setEndAt(request.getEndAt());
        session.setStatus(SessionStatus.PLANNED);
        session.setPartnershipId(request.getPartnershipId());
        session.setOrganizerId(request.getOrganizerId());
        session.setPartnerId(request.getPartnerId());
        Session saved = sessionRepository.save(session);
        return toResponse(saved);

    }

    @Override
    @Transactional(readOnly = true)
    public SessionResponse getById(Long id, String requesterId) {
        Session session = getSessionOrThrow(id);
        assertParticipant(session, requesterId);
        return toResponse(session);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponse> listUserSessions(String userId, SessionStatus status, LocalDateTime from, LocalDateTime to) {
        List<Session> sessions;
        if (from != null && to != null) {
            sessions = sessionRepository.findUserSessionsBetween(userId, from, to);
        } else if (status != null) {
            sessions = sessionRepository.findByOrganizerIdOrPartnerIdAndStatus(userId, userId, status);
        } else {
            sessions = sessionRepository.findByOrganizerIdOrPartnerId(userId, userId);
        }
        return sessions.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public SessionResponse update(Long id, String requesterId, UpdateSessionRequest request) {
        Session session = getSessionOrThrow(id);
        assertParticipant(session, requesterId);
        assertPlanned(session);
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setType(request.getType());
        session.setLocationOrLink(request.getLocationOrLink());
        session.setStartAt(request.getStartAt());
        session.setEndAt(request.getEndAt());
        return toResponse(session);
    }

    @Override
    public SessionResponse reschedule(Long id, String requesterId, RescheduleRequest request) {
        Session session = getSessionOrThrow(id);
        assertParticipant(session, requesterId);
        assertPlanned(session);
        session.setStartAt(request.getStartAt());
        session.setEndAt(request.getEndAt());
        return toResponse(session);
    }

    @Override
    public SessionResponse cancel(Long id, String requesterId) {
        Session session = getSessionOrThrow(id);
        assertParticipant(session, requesterId);
        assertPlanned(session);
        session.setStatus(SessionStatus.CANCELED);
        return toResponse(session);
    }

    @Override
    public SessionResponse complete(Long id, String requesterId) {
        Session session = getSessionOrThrow(id);
        assertParticipant(session, requesterId);
        assertPlanned(session);
        session.setStatus(SessionStatus.COMPLETED);
        return toResponse(session);
    }

    private Session getSessionOrThrow(Long id) {
        Optional<Session> optional = sessionRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("Session not found: " + id);
        }
        return optional.get();
    }

    private void assertParticipant(Session session, String requesterId) {
        if (requesterId == null ||
            (!requesterId.equals(session.getOrganizerId()) && !requesterId.equals(session.getPartnerId()))) {
            throw new AccessDeniedException("Requester is not a participant of this session");
        }
    }

    private void assertPlanned(Session session) {
        if (session.getStatus() != SessionStatus.PLANNED) {
            throw new IllegalStateException("Session status must be PLANNED to perform this action");
        }
    }

    private SessionResponse toResponse(Session s) {
        SessionResponse r = new SessionResponse();
        r.setId(s.getId());
        r.setTitle(s.getTitle());
        r.setDescription(s.getDescription());
        r.setType(s.getType());
        r.setLocationOrLink(s.getLocationOrLink());
        r.setStartAt(s.getStartAt());
        r.setEndAt(s.getEndAt());
        r.setStatus(s.getStatus());
        r.setPartnershipId(s.getPartnershipId());
        r.setOrganizerId(s.getOrganizerId());
        r.setPartnerId(s.getPartnerId());
        r.setCreatedAt(s.getCreatedAt());
        r.setUpdatedAt(s.getUpdatedAt());
        return r;
    }

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    public static class AccessDeniedException extends RuntimeException {
        public AccessDeniedException(String message) {
            super(message);
        }
    }
}


