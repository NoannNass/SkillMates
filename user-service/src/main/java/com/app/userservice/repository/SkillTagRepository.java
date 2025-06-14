package com.app.userservice.repository;

import com.app.userservice.model.SkillTag;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SkillTagRepository extends MongoRepository<SkillTag, String> {

    boolean existsByName(String name);
    boolean existsByNameAndCategory(String name, String category);
    SkillTag findByName(String name);
    SkillTag findByNameAndCategory(String name, String category);

    List<SkillTag> findByNameContainingIgnoreCase(String name);
}
