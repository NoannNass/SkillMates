package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.model.Partnership;

@Repository
public interface PartnershipRepository extends JpaRepository<Partnership, Long> {
    List<Partnership> findByRequesterIdOrRequestedId(String requesterId, String requestedId);
    boolean existsByRequesterIdAndRequestedId(String requesterId, String requestedId);
} 