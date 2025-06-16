package com.app.service;

import java.util.List;
import java.util.Optional;

import com.app.dto.ApiResponse;
import com.app.dto.CreatePartnershipRequestDTO;
import com.app.dto.PartnershipDTO;
import com.app.dto.UserSuggestionDTO;
import com.app.model.Partnership;
import com.app.model.PartnershipStatus;

/**
 * Service pour la gestion des partenariats
 */
public interface PartnershipService {

    /**
     * Crée une nouvelle demande de partenariat
     * @param requesterId ID de l'utilisateur demandeur
     * @param requestedId ID de l'utilisateur destinataire
     * @param message Message d'accompagnement
     * @return le partenariat créé
     */
    Partnership createPartnershipRequest(String requesterId, String requestedId, String message);

    /**
     * Récupère tous les partenariats
     * @return liste des partenariats
     */
    List<Partnership> getAllPartnerships();

    /**
     * Récupère un partenariat par son ID
     * @param id ID du partenariat
     * @return le partenariat trouvé (optionnel)
     */
    Optional<Partnership> getPartnershipById(Long id);

    /**
     * Récupère tous les partenariats d'un utilisateur
     * @param userId ID de l'utilisateur
     * @return liste des partenariats de l'utilisateur
     */
    List<Partnership> getPartnershipsByUserId(String userId);

    /**
     * Met à jour le statut d'un partenariat
     * @param partnershipId ID du partenariat
     * @param status Nouveau statut
     * @return le partenariat mis à jour
     */
    Partnership updatePartnershipStatus(Long partnershipId, PartnershipStatus status);

    /**
     * Supprime un partenariat
     * @param id ID du partenariat à supprimer
     */
    void deletePartnership(Long id);

    /**
     * Vérifie si un partenariat existe entre deux utilisateurs
     * @param userId1 ID du premier utilisateur
     * @param userId2 ID du deuxième utilisateur
     * @return true si un partenariat existe
     */
    boolean existsPartnershipBetweenUsers(String userId1, String userId2);

    ApiResponse<PartnershipDTO> createPartnershipRequest(CreatePartnershipRequestDTO request);
    ApiResponse<List<PartnershipDTO>> getUserPartnerships(String userId);
    ApiResponse<PartnershipDTO> acceptPartnership(Long partnershipId);
    ApiResponse<PartnershipDTO> denyPartnership(Long partnershipId);
    ApiResponse<PartnershipDTO> cancelPartnership(Long partnershipId);
    ApiResponse<PartnershipDTO> endPartnership(Long partnershipId);
    ApiResponse<List<UserSuggestionDTO>> getPartnershipSuggestions(String userId);
    ApiResponse<PartnershipDTO> getPartnership(Long partnershipId);
} 