package com.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "partnerships")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Partnership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "requester_id", nullable = false)
    private String requesterId;
    
    @Column(name = "requested_id", nullable = false) 
    private String requestedId;
    
    @Enumerated(EnumType.STRING)
    private PartnershipStatus status;
    
    private String message;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private LocalDateTime acceptedAt;
    
    private LocalDateTime endedAt;
    
    @OneToMany(mappedBy = "partnership", cascade = CascadeType.ALL)
    private List<PartnershipGoal> goals = new ArrayList<>();
} 