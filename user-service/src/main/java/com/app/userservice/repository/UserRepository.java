package com.app.userservice.repository;

import com.app.userservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findBySkillsName(String skillName);
    List<User> findByInterestsName(String interestName);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
