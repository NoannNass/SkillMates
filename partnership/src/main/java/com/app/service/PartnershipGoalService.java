package com.app.service;

import java.util.List;
import java.util.Optional;

import com.app.model.PartnershipGoal;

/**
 * Service pour la gestion des objectifs de partenariat
 */
public interface PartnershipGoalService {

    /**
     * Crée un nouvel objectif de partenariat
     * @param goal objectif à créer
     * @return l'objectif créé
     */
    PartnershipGoal createGoal(PartnershipGoal goal);

    /**
     * Récupère tous les objectifs
     * @return liste des objectifs
     */
    List<PartnershipGoal> getAllGoals();

    /**
     * Récupère tous les objectifs d'un partenariat
     * @param partnershipId ID du partenariat
     * @return liste des objectifs du partenariat
     */
    List<PartnershipGoal> getGoalsByPartnershipId(Long partnershipId);

    /**
     * Récupère un objectif par son ID
     * @param id ID de l'objectif
     * @return l'objectif trouvé (optionnel)
     */
    Optional<PartnershipGoal> getGoalById(Long id);

    /**
     * Met à jour un objectif
     * @param id ID de l'objectif
     * @param goal nouvelles informations
     * @return l'objectif mis à jour
     */
    PartnershipGoal updateGoal(Long id, PartnershipGoal goal);

    /**
     * Met à jour la progression d'un objectif
     * @param id ID de l'objectif
     * @param progressPercentage nouveau pourcentage de progression
     * @return l'objectif mis à jour
     */
    PartnershipGoal updateProgress(Long id, Integer progressPercentage);

    /**
     * Supprime un objectif
     * @param id ID de l'objectif à supprimer
     */
    void deleteGoal(Long id);
} 