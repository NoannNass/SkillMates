package com.app.sessionservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.sessionservice.model.Session;
import com.app.sessionservice.model.enums.SessionStatus;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByOrganizerIdOrPartnerId(String organizerId, String partnerId);

    List<Session> findByOrganizerIdOrPartnerIdAndStatus(String organizerId, String partnerId, SessionStatus status);

    @Query("select s from Session s where (s.organizerId = :userId or s.partnerId = :userId) and s.startAt >= :from and s.endAt <= :to")
    List<Session> findUserSessionsBetween(@Param("userId") String userId,
                                          @Param("from") LocalDateTime from,
                                          @Param("to") LocalDateTime to);
}


