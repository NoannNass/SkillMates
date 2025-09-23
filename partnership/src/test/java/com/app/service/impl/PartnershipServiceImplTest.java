package com.app.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.app.client.UserClient;
import com.app.model.Partnership;
import com.app.model.PartnershipStatus;
import com.app.repository.PartnershipRepository;

class PartnershipServiceImplTest {

    @Mock
    private PartnershipRepository partnershipRepository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private PartnershipServiceImpl partnershipService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPartnershipRequest_succeeds_whenNoExistingPartnership() {
        String requesterId = "user-1";
        String requestedId = "user-2";
        String message = "Salut, on collabore ?";

        when(partnershipRepository.existsByRequesterIdAndRequestedId(requesterId, requestedId)).thenReturn(false);
        when(partnershipRepository.existsByRequesterIdAndRequestedId(requestedId, requesterId)).thenReturn(false);

        when(partnershipRepository.save(any(Partnership.class))).thenAnswer(invocation -> {
            Partnership p = invocation.getArgument(0);
            p.setId(1L);
            p.setCreatedAt(LocalDateTime.now());
            p.setUpdatedAt(LocalDateTime.now());
            return p;
        });

        Partnership result = partnershipService.createPartnershipRequest(requesterId, requestedId, message);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getRequesterId()).isEqualTo(requesterId);
        assertThat(result.getRequestedId()).isEqualTo(requestedId);
        assertThat(result.getMessage()).isEqualTo(message);
        assertThat(result.getStatus()).isEqualTo(PartnershipStatus.PENDING);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    void createPartnershipRequest_throws_whenExistingPartnershipBetweenUsers() {
        String requesterId = "user-1";
        String requestedId = "user-2";

        when(partnershipRepository.existsByRequesterIdAndRequestedId(requesterId, requestedId)).thenReturn(true);
        when(partnershipRepository.existsByRequesterIdAndRequestedId(requestedId, requesterId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                partnershipService.createPartnershipRequest(requesterId, requestedId, "msg"));
    }
}

