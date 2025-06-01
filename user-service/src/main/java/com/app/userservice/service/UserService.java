package com.app.userservice.service;

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


    User updateUserPersonalInfo(String userId, String username, String bio);
}
