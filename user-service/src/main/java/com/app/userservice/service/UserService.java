package com.app.userservice.service;

import com.app.userservice.dto.UserDto;
import com.app.userservice.model.InterestTag;
import com.app.userservice.model.LearningObjective;
import com.app.userservice.model.ProfileCompletionStatus;
import com.app.userservice.model.SkillTag;
import com.app.userservice.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UserService {


    User registerUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(String id);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByUsername(String username);

    User updateUser(String id, User user);

    void deleteUser(String id);

    List<User> findUsersBySkill(String skillName);

    List<User> findUsersByInterest(String interestName);

    User updatePersonalInfo(String userId, String username, String bio, String profilePictureUrl);

    User updateSkills(String userId, List<SkillTag> skills);

    User updateInterests(String userId, List<InterestTag> interests);

    User updateLearningObjectives(String userId, List<LearningObjective> objectives);

    ProfileCompletionStatus getProfileCompletionStatus(String userId);

    List<UserDto> searchUsers(String query);

}
