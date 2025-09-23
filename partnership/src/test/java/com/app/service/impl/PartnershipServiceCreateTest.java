package com.app.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.app.client.UserClient;
import com.app.dto.ApiResponse;
import com.app.dto.CreatePartnershipRequestDTO;
import com.app.dto.PartnershipDTO;
import com.app.model.Partnership;
import com.app.model.PartnershipStatus;
import com.app.repository.PartnershipRepository;

class PartnershipServiceCreateTest {

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
    void createPartnershipRequest_dto_succeeds_whenValidRequest() {

        CreatePartnershipRequestDTO requestDTO = new CreatePartnershipRequestDTO();
        requestDTO.setRequesterId("user-1");
        requestDTO.setRequestedId("user-2");
        requestDTO.setMessage("Collaborons ensemble sur ce projet");

        when(partnershipRepository.existsByRequesterIdAndRequestedId("user-1", "user-2")).thenReturn(false);
        when(partnershipRepository.existsByRequesterIdAndRequestedId("user-2", "user-1")).thenReturn(false);

        when(partnershipRepository.save(any(Partnership.class))).thenAnswer(invocation -> {
            Partnership p = invocation.getArgument(0);
            p.setId(1L);
            p.setCreatedAt(LocalDateTime.now());
            p.setUpdatedAt(LocalDateTime.now());
            return p;
        });

        // Act
        ApiResponse<PartnershipDTO> response = partnershipService.createPartnershipRequest(requestDTO);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).contains("succès");
        
        PartnershipDTO result = response.getData();
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getRequesterId()).isEqualTo("user-1");
        assertThat(result.getRequestedId()).isEqualTo("user-2");
        assertThat(result.getMessage()).isEqualTo("Collaborons ensemble sur ce projet");
        assertThat(result.getStatus()).isEqualTo(PartnershipStatus.PENDING);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    void createPartnershipRequest_dto_fails_whenRequestingPartnershipWithSelf() {
        // Arrange
        CreatePartnershipRequestDTO requestDTO = new CreatePartnershipRequestDTO();
        requestDTO.setRequesterId("user-1");
        requestDTO.setRequestedId("user-1");
        requestDTO.setMessage("Message test");

        // Act
        ApiResponse<PartnershipDTO> response = partnershipService.createPartnershipRequest(requestDTO);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).contains("vous-même");
        assertThat(response.getData()).isNull();
    }

    @Test
    void createPartnershipRequest_dto_fails_whenPartnershipAlreadyExists() {
        // Arrange
        CreatePartnershipRequestDTO requestDTO = new CreatePartnershipRequestDTO();
        requestDTO.setRequesterId("user-1");
        requestDTO.setRequestedId("user-2");
        requestDTO.setMessage("Message test");

        when(partnershipRepository.existsByRequesterIdAndRequestedId("user-1", "user-2")).thenReturn(true);

        // Act
        ApiResponse<PartnershipDTO> response = partnershipService.createPartnershipRequest(requestDTO);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).contains("existe déjà");
        assertThat(response.getData()).isNull();
    }

    @Test
    void createPartnershipRequest_dto_fails_whenRequesterIdIsNull() {
        // Arrange
        CreatePartnershipRequestDTO requestDTO = new CreatePartnershipRequestDTO();
        requestDTO.setRequesterId(null);
        requestDTO.setRequestedId("user-2");
        requestDTO.setMessage("Message test");

        // Act
        ApiResponse<PartnershipDTO> response = partnershipService.createPartnershipRequest(requestDTO);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).contains("demandeur");
        assertThat(response.getData()).isNull();
    }

    @Test
    void createPartnershipRequest_dto_fails_whenRequestedIdIsNull() {
        // Arrange
        CreatePartnershipRequestDTO requestDTO = new CreatePartnershipRequestDTO();
        requestDTO.setRequesterId("user-1");
        requestDTO.setRequestedId(null);
        requestDTO.setMessage("Message test");

        // Act
        ApiResponse<PartnershipDTO> response = partnershipService.createPartnershipRequest(requestDTO);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).contains("destinataire");
        assertThat(response.getData()).isNull();
    }
}
