package com.foodbridge.admin.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.foodbridge.common.enums.Role;
import com.foodbridge.common.enums.UserStatus;
import com.foodbridge.user.entity.User;
import com.foodbridge.user.repository.UserRepository;

@Component
public class AdminDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminDataInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        boolean adminExists =
                userRepository.findAll()
                        .stream()
                        .anyMatch(user -> user.getRole() == Role.ADMIN);

        if (adminExists) {
            return;
        }

        User admin = new User();

        admin.setFullName("FoodBridge Admin");
        admin.setEmail("admin@foodbridge.com");
        admin.setPhoneNumber("9000000000");

        admin.setPassword(
                passwordEncoder.encode("Admin@123")
        );

        admin.setRole(Role.ADMIN);
        admin.setStatus(UserStatus.ACTIVE);

        userRepository.save(admin);

        System.out.println(
                "=========================================="
        );
        System.out.println(
                "FoodBridge ADMIN account created"
        );
        System.out.println(
                "Email: admin@foodbridge.com"
        );
        System.out.println(
                "Password: Admin@123"
        );
        System.out.println(
                "=========================================="
        );
    }
}