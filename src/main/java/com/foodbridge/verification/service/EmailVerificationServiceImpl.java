package com.foodbridge.verification.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.foodbridge.common.enums.UserStatus;
import com.foodbridge.user.entity.User;
import com.foodbridge.user.repository.UserRepository;
import com.foodbridge.verification.entity.EmailVerification;
import com.foodbridge.verification.repository.EmailVerificationRepository;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public EmailVerificationServiceImpl(
            EmailVerificationRepository emailVerificationRepository,
            UserRepository userRepository,
            EmailService emailService) {

        this.emailVerificationRepository = emailVerificationRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public void sendOtp(String email) {

        Random random = new Random();

        String otp = String.format(
                "%06d",
                random.nextInt(1000000)
        );

        EmailVerification verification = new EmailVerification();

        verification.setEmail(email);
        verification.setOtp(otp);
        verification.setExpiresAt(
                LocalDateTime.now().plusMinutes(5)
        );
        verification.setVerified(false);

        emailVerificationRepository.save(verification);

        String subject = "FoodBridge Email Verification";

        String body = "Your FoodBridge OTP is: " + otp
                + "\n\nThis OTP is valid for 5 minutes.";

        emailService.sendEmail(email, subject, body);
    }

    @Override
    public void verifyOtp(String email, String otp) {

        EmailVerification verification =
                emailVerificationRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("OTP not found"));

        if (verification.isVerified()) {
            throw new RuntimeException("Email already verified");
        }

        if (verification.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException("OTP expired");
        }

        if (!verification.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        verification.setVerified(true);
        emailVerificationRepository.save(verification);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);
    }
}