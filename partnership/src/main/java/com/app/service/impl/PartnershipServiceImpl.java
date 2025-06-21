package com.app.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.client.UserClient;
import com.app.dto.ApiResponse;
import com.app.dto.CreatePartnershipRequestDTO;
import com.app.dto.PartnershipDTO;
import com.app.dto.PartnershipGoalDTO;
import com.app.dto.UserDto;
import com.app.dto.UserSuggestionDTO;
import com.app.model.Partnership;
import com.app.model.PartnershipStatus;
import com.app.repository.PartnershipRepository;
import com.app.service.PartnershipService;

@Service
public class PartnershipServiceImpl implements PartnershipService {

    private final PartnershipRepository partnershipRepository;
    private final UserClient userClient;

    @Autowired
    public PartnershipServiceImpl(PartnershipRepository partnershipRepository, UserClient userClient) {
        this.partnershipRepository = partnershipRepository;
        this.userClient = userClient;
    }

    @Override
    public Partnership createPartnershipRequest(String requesterId, String requestedId, String message) {
        // Vérifier si un partenariat existe déjà entre ces utilisateurs
        if (existsPartnershipBetweenUsers(requesterId, requestedId)) {
            throw new IllegalArgumentException("Un partenariat existe déjà entre ces utilisateurs");
        }

        Partnership partnership = new Partnership();
        partnership.setRequesterId(requesterId);
        partnership.setRequestedId(requestedId);
        partnership.setMessage(message);
        partnership.setStatus(PartnershipStatus.PENDING);
        partnership.setCreatedAt(LocalDateTime.now());
        partnership.setUpdatedAt(LocalDateTime.now());

        return partnershipRepository.save(partnership);
    }

    @Override
    public List<Partnership> getAllPartnerships() {
        return partnershipRepository.findAll();
    }

    @Override
    public Optional<Partnership> getPartnershipById(Long id) {
        return partnershipRepository.findById(id);
    }

    @Override
    public List<Partnership> getPartnershipsByUserId(String userId) {
        return partnershipRepository.findByRequesterIdOrRequestedId(userId, userId);
    }

    @Override
    public Partnership updatePartnershipStatus(Long partnershipId, PartnershipStatus status) {
        Partnership partnership = getPartnershipById(partnershipId)
                .orElseThrow(() -> new IllegalArgumentException("Partenariat non trouvé avec l'ID: " + partnershipId));

        partnership.setStatus(status);
        partnership.setUpdatedAt(LocalDateTime.now());

        if (status == PartnershipStatus.ACCEPTED) {
            partnership.setAcceptedAt(LocalDateTime.now());
        } else if (status == PartnershipStatus.ENDED) {
            partnership.setEndedAt(LocalDateTime.now());
        }

        return partnershipRepository.save(partnership);
    }

    @Override
    public void deletePartnership(Long id) {
        if (!partnershipRepository.existsById(id)) {
            throw new IllegalArgumentException("Partenariat non trouvé avec l'ID: " + id);
        }
        partnershipRepository.deleteById(id);
    }

    @Override
    public boolean existsPartnershipBetweenUsers(String userId1, String userId2) {
        return partnershipRepository.existsByRequesterIdAndRequestedId(userId1, userId2) ||
               partnershipRepository.existsByRequesterIdAndRequestedId(userId2, userId1);
    }

    @Override
    public ApiResponse<PartnershipDTO> createPartnershipRequest(CreatePartnershipRequestDTO request) {
        try {
            // Créer un nouveau partenariat
            Partnership partnership = new Partnership();
            partnership.setRequestedId(request.getRequestedId());
            partnership.setMessage(request.getMessage());
            partnership.setStatus(PartnershipStatus.PENDING);
            partnership.setCreatedAt(LocalDateTime.now());
            partnership.setUpdatedAt(LocalDateTime.now());

            // Sauvegarder le partenariat
            partnership = partnershipRepository.save(partnership);
            
            PartnershipDTO dto = convertToDTO(partnership);
            return new ApiResponse<>(true, "Demande de partenariat créée avec succès", dto);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<List<PartnershipDTO>> getUserPartnerships(String userId) {
        try {
            List<Partnership> partnerships = getPartnershipsByUserId(userId);
            List<PartnershipDTO> dtos = partnerships.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            
            return new ApiResponse<>(true, "Partenariats récupérés avec succès", dtos);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Erreur lors de la récupération des partenariats", null);
        }
    }

    @Override
    public ApiResponse<PartnershipDTO> acceptPartnership(Long partnershipId) {
        try {
            Partnership partnership = updatePartnershipStatus(partnershipId, PartnershipStatus.ACCEPTED);
            PartnershipDTO dto = convertToDTO(partnership);
            return new ApiResponse<>(true, "Partenariat accepté avec succès", dto);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<PartnershipDTO> denyPartnership(Long partnershipId) {
        try {
            Partnership partnership = updatePartnershipStatus(partnershipId, PartnershipStatus.DENIED);
            PartnershipDTO dto = convertToDTO(partnership);
            return new ApiResponse<>(true, "Partenariat refusé avec succès", dto);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<PartnershipDTO> cancelPartnership(Long partnershipId) {
        try {
            Partnership partnership = updatePartnershipStatus(partnershipId, PartnershipStatus.CANCELLED);
            PartnershipDTO dto = convertToDTO(partnership);
            return new ApiResponse<>(true, "Partenariat annulé avec succès", dto);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<PartnershipDTO> endPartnership(Long partnershipId) {
        try {
            Partnership partnership = updatePartnershipStatus(partnershipId, PartnershipStatus.ENDED);
            PartnershipDTO dto = convertToDTO(partnership);
            return new ApiResponse<>(true, "Partenariat terminé avec succès", dto);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<List<UserSuggestionDTO>> getPartnershipSuggestions(String userId) {
        // Ancienne logique supprimée, on invite à utiliser la recherche par username
        return new ApiResponse<>(true, "Utilisez la recherche par username pour trouver un utilisateur.", List.of());
    }

    @Override
    public ApiResponse<PartnershipDTO> getPartnership(Long partnershipId) {
        try {
            Partnership partnership = getPartnershipById(partnershipId)
                .orElseThrow(() -> new IllegalArgumentException("Partenariat non trouvé avec l'ID: " + partnershipId));
            
            PartnershipDTO dto = convertToDTO(partnership);
            return new ApiResponse<>(true, "Partenariat récupéré avec succès", dto);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        }
    }

    // Nouvelle méthode : recherche d'utilisateur par username
    public ApiResponse<UserDto> searchUserByUsername(String username) {
        try {
            return userClient.getUserByUsername(username);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Erreur lors de la recherche de l'utilisateur par username", null);
        }
    }

    // Méthode utilitaire pour convertir Partnership en PartnershipDTO
    private PartnershipDTO convertToDTO(Partnership partnership) {
        PartnershipDTO dto = new PartnershipDTO();
        dto.setId(partnership.getId());
        dto.setRequesterId(partnership.getRequesterId());
        dto.setRequestedId(partnership.getRequestedId());
        dto.setStatus(partnership.getStatus());
        dto.setMessage(partnership.getMessage());
        dto.setCreatedAt(partnership.getCreatedAt());
        dto.setUpdatedAt(partnership.getUpdatedAt());
        dto.setAcceptedAt(partnership.getAcceptedAt());
        dto.setEndedAt(partnership.getEndedAt());
        
        // Convertir les objectifs si présents
        if (partnership.getGoals() != null) {
            dto.setGoals(partnership.getGoals().stream()
                .map(goal -> {
                    PartnershipGoalDTO goalDTO = new PartnershipGoalDTO();
                    goalDTO.setId(goal.getId());
                    goalDTO.setTitle(goal.getTitle());
                    goalDTO.setDescription(goal.getDescription());
                    goalDTO.setProgressPercentage(goal.getProgressPercentage());
                    goalDTO.setTargetDate(goal.getTargetDate());
                    return goalDTO;
                })
                .collect(Collectors.toList()));
        }
        
        return dto;
    }
} 