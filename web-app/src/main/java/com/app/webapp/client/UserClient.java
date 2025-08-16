package com.app.webapp.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.webapp.dto.ApiResponse;
import com.app.webapp.dto.InterestTagDTO;
import com.app.webapp.dto.LearningObjectiveDTO;
import com.app.webapp.dto.PersonalInfoDTO;
import com.app.webapp.dto.ProfileCompletionStatus;
import com.app.webapp.dto.SkillTagDTO;
import com.app.webapp.dto.UserDto;

@FeignClient(name = "user-service", path = "/api/users")
public interface UserClient {
    @PostMapping("/create")
    ApiResponse<UserDto> createUser(@RequestBody UserDto userProfileDTO);

    @GetMapping
    ApiResponse<List<UserDto>> getAllUsers();

    @GetMapping("/{id}")
    ApiResponse<UserDto> getUserById(@PathVariable("id") String id);

    @GetMapping("/username/{username}")
    ApiResponse<UserDto> getUserByUsername(@PathVariable("username") String username);

    @GetMapping("/email")
    ApiResponse<UserDto> getUserByEmail(@RequestParam("email") String email);

    @PutMapping("/{id}")
    ApiResponse<UserDto> updateUser(@PathVariable("id") String id, @RequestBody UserDto userProfileDTO);

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteUser(@PathVariable("id") String id);

    @GetMapping("/skills/{skillName}")
    ApiResponse<List<UserDto>> getUsersBySkill(@PathVariable("skillName") String skillName);

    @GetMapping("/interests/{interestName}")
    ApiResponse<List<UserDto>> getUsersByInterest(@PathVariable("interestName") String interestName);

    @PutMapping("/profile/personal-info")
    ApiResponse<UserDto> updatePersonalInfo(@RequestParam("userId") String userId, @RequestBody PersonalInfoDTO personalInfoDTO);

    @PutMapping("/profile/skills")
    ApiResponse<UserDto> updateSkills(@RequestParam("userId") String userId, @RequestBody List<SkillTagDTO> skillDTOs);

    @PutMapping("/profile/interests")
    ApiResponse<UserDto> updateInterests(@RequestParam("userId") String userId, @RequestBody List<InterestTagDTO> interestDTOs);

    @PutMapping("/profile/objectives")
    ApiResponse<UserDto> updateLearningObjectives(@RequestParam("userId") String userId, @RequestBody List<LearningObjectiveDTO> objectiveDTOs);

    @GetMapping("/profile/completion")
    ApiResponse<ProfileCompletionStatus> getProfileCompletionStatus(@RequestParam("userId") String userId);

    @GetMapping("/search")
    ApiResponse<List<UserDto>> searchUsers(@RequestParam String query);
}
