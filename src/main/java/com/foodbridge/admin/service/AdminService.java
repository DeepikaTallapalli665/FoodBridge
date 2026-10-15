package com.foodbridge.admin.service;

import java.util.List;

import com.foodbridge.user.dto.response.UserResponse;

public interface AdminService {

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long userId);
    UserResponse blockUser(Long userId);

    UserResponse unblockUser(Long userId);
    
}