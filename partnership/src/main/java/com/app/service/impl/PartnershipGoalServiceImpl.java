package com.app.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.model.PartnershipGoal;
import com.app.repository.PartnershipGoalRepository;
import com.app.service.PartnershipGoalService;

@Service
public class PartnershipGoalServiceImpl implements PartnershipGoalService {

    private final PartnershipGoalRepository goalRepository;

    @Autowired
    public PartnershipGoalServiceImpl(PartnershipGoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    @Override
    public PartnershipGoal createGoal(PartnershipGoal goal) {
        // Initialiser la progression si elle n'est pas définie
        if (goal.getProgressPercentage() == null) {
            goal.setProgressPercentage(0);
        }

        // Initialiser la date de création
        goal.setCreatedAt(LocalDateTime.now());

        return goalRepository.save(goal);
    }

    @Override
    public List<PartnershipGoal> getAllGoals() {
        return goalRepository.findAll();
    }

    @Override
    public List<PartnershipGoal> getGoalsByPartnershipId(Long partnershipId) {
        return goalRepository.findByPartnershipId(partnershipId);
    }

    @Override
    public Optional<PartnershipGoal> getGoalById(Long id) {
        return goalRepository.findById(id);
    }

    @Override
    public PartnershipGoal updateGoal(Long id, PartnershipGoal goal) {
        PartnershipGoal existingGoal = getGoalById(id)
                .orElseThrow(() -> new IllegalArgumentException("Objectif non trouvé avec l'ID: " + id));

        // Conserver la date de création
        goal.setCreatedAt(existingGoal.getCreatedAt());

        // S'assurer que l'ID reste le même
        goal.setId(id);

        return goalRepository.save(goal);
    }

    @Override
    public PartnershipGoal updateProgress(Long id, Integer progressPercentage) {
        PartnershipGoal goal = getGoalById(id)
                .orElseThrow(() -> new IllegalArgumentException("Objectif non trouvé avec l'ID: " + id));

        // Valider le pourcentage de progression
        if (progressPercentage < 0 || progressPercentage > 100) {
            throw new IllegalArgumentException("Le pourcentage de progression doit être compris entre 0 et 100");
        }

        goal.setProgressPercentage(progressPercentage);
        return goalRepository.save(goal);
    }

    @Override
    public void deleteGoal(Long id) {
        if (!goalRepository.existsById(id)) {
            throw new IllegalArgumentException("Objectif non trouvé avec l'ID: " + id);
        }
        goalRepository.deleteById(id);
    }
} 