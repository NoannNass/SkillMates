package com.app.webapp.client;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.webapp.dto.session.CreateSessionRequest;
import com.app.webapp.dto.session.RescheduleRequest;
import com.app.webapp.dto.session.SessionResponse;
import com.app.webapp.dto.session.SessionStatus;
import com.app.webapp.dto.session.UpdateSessionRequest;

@FeignClient(name = "session-service", path = "/sessions")
public interface SessionClient {

    @PostMapping
    SessionResponse create(@RequestBody CreateSessionRequest request);

    @GetMapping
    List<SessionResponse> list(
            @RequestParam("userId") String userId,
            @RequestParam(value = "status", required = false) SessionStatus status,
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    );

    @GetMapping("/{id}")
    SessionResponse getById(@PathVariable("id") Long id, @RequestParam("requesterId") String requesterId);

    @PatchMapping("/{id}")
    SessionResponse update(@PathVariable("id") Long id,
                           @RequestParam("requesterId") String requesterId,
                           @RequestBody UpdateSessionRequest request);

    @PostMapping("/{id}:reschedule")
    SessionResponse reschedule(@PathVariable("id") Long id,
                               @RequestParam("requesterId") String requesterId,
                               @RequestBody RescheduleRequest request);

    @PostMapping("/{id}:cancel")
    SessionResponse cancel(@PathVariable("id") Long id,
                           @RequestParam("requesterId") String requesterId);

    @PostMapping("/{id}:complete")
    SessionResponse complete(@PathVariable("id") Long id,
                             @RequestParam("requesterId") String requesterId);
}


