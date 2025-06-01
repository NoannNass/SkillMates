package com.app.userservice.repository;

import com.app.userservice.model.InterestTag;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface InterestTagRepository extends MongoRepository<InterestTag, String> {
    Optional<InterestTag> findByName(String name);
    //Recherche des tags d'intérêt par nom partiel (insensible à la casse) pour la recherche en temps réel et les suggestions automatiques
    List<InterestTag> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
    Optional<InterestTag> findById(String id);
}
