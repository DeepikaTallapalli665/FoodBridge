
package com.foodbridge.admin.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodbridge.admin.service.AdminService;
import com.foodbridge.common.enums.UserStatus;
import com.foodbridge.exception.ResourceNotFoundException;
import com.foodbridge.user.dto.response.UserResponse;
import com.foodbridge.user.entity.User;
import com.foodbridge.user.repository.UserRepository;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {

        User user = getUserOrThrow(userId);

        return mapToUserResponse(user);
    }

    @Override
    public UserResponse blockUser(Long userId) {

        User user = getUserOrThrow(userId);

        if (user.getRole().name().equals("ADMIN")) {
            throw new IllegalArgumentException(
                    "Admin accounts cannot be blocked");
        }

        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new IllegalStateException(
                    "User is already blocked");
        }

        user.setStatus(UserStatus.BLOCKED);

        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    @Override
    public UserResponse unblockUser(Long userId) {

        User user = getUserOrThrow(userId);

        if (user.getRole().name().equals("ADMIN")) {
            throw new IllegalArgumentException(
                    "Admin accounts cannot be unblocked through this operation");
        }

        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new IllegalStateException(
                    "User is already active");
        }

        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    private User getUserOrThrow(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));
    }

    private UserResponse mapToUserResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}
