package com.app.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.dto.ApiResponse;
import com.app.dto.UserDto;

@FeignClient(name = "user-service", url = "/api/users")
public interface UserClient {
    @GetMapping
    ApiResponse<List<UserDto>> getAllUsers();

    @GetMapping("/{id}")
    ApiResponse<UserDto> getUserById(@PathVariable("id") String id);

    @GetMapping("/username/{username}")
    ApiResponse<UserDto> getUserByUsername(@PathVariable("username") String username);
} 