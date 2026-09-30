package com.foodbridge.auth.service;

import com.foodbridge.auth.dto.request.LoginRequest;
import com.foodbridge.auth.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}