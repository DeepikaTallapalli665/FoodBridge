package com.foodbridge.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodbridge.admin.service.AdminService;
import com.foodbridge.user.dto.response.UserResponse;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users =
                adminService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long userId) {

        UserResponse user =
                adminService.getUserById(userId);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{userId}/block")
    public ResponseEntity<UserResponse> blockUser(
            @PathVariable Long userId) {

        UserResponse user =
                adminService.blockUser(userId);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{userId}/unblock")
    public ResponseEntity<UserResponse> unblockUser(
            @PathVariable Long userId) {

        UserResponse user =
                adminService.unblockUser(userId);

        return ResponseEntity.ok(user);
    }
}