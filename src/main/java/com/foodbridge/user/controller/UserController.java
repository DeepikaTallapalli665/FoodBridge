package com.foodbridge.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodbridge.user.dto.request.ChangePasswordRequest;
import com.foodbridge.user.dto.request.UserRegistrationRequest;
import com.foodbridge.user.dto.request.UserUpdateRequest;
import com.foodbridge.user.dto.response.UserResponse;
import com.foodbridge.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
	    this.userService = userService;
	}
	@GetMapping("/me")
	public ResponseEntity<UserResponse> getMyProfile(
	        Authentication authentication) {

	    String email = authentication.getName();

	    UserResponse response = userService.getMyProfile(email);

	    return ResponseEntity.ok(response);
	}
	@PutMapping("/me")
	public ResponseEntity<UserResponse> updateMyProfile(
	        @Valid @RequestBody UserUpdateRequest request,
	        Authentication authentication) {

	    String email = authentication.getName();

	    UserResponse response =
	            userService.updateMyProfile(email, request);

	    return ResponseEntity.ok(response);
	}
	@PutMapping("/me/password")
	public ResponseEntity<String> changePassword(
	        @Valid @RequestBody ChangePasswordRequest request,
	        Authentication authentication) {

	    String email = authentication.getName();

	    userService.changePassword(email, request);

	    return ResponseEntity.ok("Password changed successfully");
	}
	@PostMapping("/register")
	public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {

	    UserResponse response = userService.registerUser(request);

	    return ResponseEntity.status(HttpStatus.CREATED).body(response);
	    }
	

}