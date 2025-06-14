package com.app.userservice.repository;

import com.app.userservice.model.LearningObjective;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LearningObjectiveRepository extends MongoRepository<LearningObjective, String> {
    boolean existsByName(String name);
    boolean existsByNameAndCategory(String name, String category);
    LearningObjective findByName(String name);
    LearningObjective findByNameAndCategory(String name, String category);
    List<LearningObjective> findByUserId(String userId);
    List<LearningObjective> findByTitleContainingIgnoreCase(String title);
    Optional<LearningObjective> findByTitleAndUserId(String title, String userId);
    boolean existsByTitleAndUserId(String title, String userId);
}
