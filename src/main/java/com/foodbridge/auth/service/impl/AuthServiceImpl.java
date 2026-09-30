package com.foodbridge.auth.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.foodbridge.auth.dto.request.LoginRequest;
import com.foodbridge.auth.dto.response.LoginResponse;
import com.foodbridge.auth.service.AuthService;
import com.foodbridge.user.entity.User;
import com.foodbridge.user.exception.InvalidCredentialsException;
import com.foodbridge.user.repository.UserRepository;
import com.foodbridge.auth.jwt.JwtService;
@Service
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	public AuthServiceImpl(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

this.userRepository = userRepository;
this.passwordEncoder = passwordEncoder;
this.jwtService = jwtService;
}

    @Override
    public LoginResponse login(LoginRequest request) {
    	Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
    	if (userOptional.isEmpty()) {
    		throw new InvalidCredentialsException("Invalid email or password");
    		}
    	 User user = userOptional.get();
    	 if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
    		 throw new InvalidCredentialsException("Invalid email or password");
    		 }
    	 String token = jwtService.generateToken(user);
    	 return new LoginResponse(
    		        token,
    		        user.getId(),
    		        user.getFullName(),
    		        user.getEmail(),
    		        user.getRole()
    		);
    	 
    	 


    }

}