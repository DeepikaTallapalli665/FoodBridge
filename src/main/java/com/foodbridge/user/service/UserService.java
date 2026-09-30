package com.foodbridge.user.service;

import com.foodbridge.user.dto.request.ChangePasswordRequest;
import com.foodbridge.user.dto.request.UserRegistrationRequest;
import com.foodbridge.user.dto.request.UserUpdateRequest;
import com.foodbridge.user.dto.response.UserResponse;

public interface UserService {

    UserResponse registerUser(UserRegistrationRequest request);
    UserResponse getMyProfile(String email);
    UserResponse updateMyProfile(String email, UserUpdateRequest request);
    void changePassword(
            String email,
            ChangePasswordRequest request);

}