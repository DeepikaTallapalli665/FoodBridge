package com.foodbridge.verification.service;

public interface EmailVerificationService {
	void sendOtp(String email);

    void verifyOtp(String email, String otp);

}
