package com.app.userservice.repository;

import com.app.userservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findBySkillsName(String skillName);
    List<User> findByInterestsName(String interestName);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email);
}
