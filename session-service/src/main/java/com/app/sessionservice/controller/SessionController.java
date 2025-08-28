package com.app.sessionservice.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.sessionservice.model.enums.SessionStatus;
import com.app.sessionservice.dto.CreateSessionRequest;
import com.app.sessionservice.dto.RescheduleRequest;
import com.app.sessionservice.dto.SessionResponse;
import com.app.sessionservice.dto.UpdateSessionRequest;
import com.app.sessionservice.service.SessionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/sessions")
@Validated
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<SessionResponse> create(@Valid @RequestBody CreateSessionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<SessionResponse>> list(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "status", required = false) SessionStatus status,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(sessionService.listUserSessions(userId, status, from, to));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionResponse> getById(@PathVariable("id") Long id,
                                                   @RequestParam("requesterId") Long requesterId) {
        return ResponseEntity.ok(sessionService.getById(id, requesterId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SessionResponse> update(@PathVariable("id") Long id,
                                                  @RequestParam("requesterId") Long requesterId,
                                                  @Valid @RequestBody UpdateSessionRequest request) {
        return ResponseEntity.ok(sessionService.update(id, requesterId, request));
    }

    @PostMapping("/{id}:reschedule")
    public ResponseEntity<SessionResponse> reschedule(@PathVariable("id") Long id,
                                                      @RequestParam("requesterId") Long requesterId,
                                                      @Valid @RequestBody RescheduleRequest request) {
        return ResponseEntity.ok(sessionService.reschedule(id, requesterId, request));
    }

    @PostMapping("/{id}:cancel")
    public ResponseEntity<SessionResponse> cancel(@PathVariable("id") Long id,
                                                  @RequestParam("requesterId") Long requesterId) {
        return ResponseEntity.ok(sessionService.cancel(id, requesterId));
    }

    @PostMapping("/{id}:complete")
    public ResponseEntity<SessionResponse> complete(@PathVariable("id") Long id,
                                                    @RequestParam("requesterId") Long requesterId) {
        return ResponseEntity.ok(sessionService.complete(id, requesterId));
    }
}
