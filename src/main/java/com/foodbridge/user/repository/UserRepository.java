
package com.foodbridge.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodbridge.common.enums.Role;
import com.foodbridge.common.enums.UserStatus;
import com.foodbridge.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

    List<User> findByRoleAndStatus(Role role, UserStatus status);
}