package com.foodbridge.user.dto.response;


import java.time.LocalDateTime;

import com.foodbridge.common.enums.Role;
import com.foodbridge.common.enums.UserStatus;

public class UserResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private Role role;

    private UserStatus status;

    private LocalDateTime createdAt;

	public UserResponse() {
		super();
	}

	public UserResponse(Long id, String fullName, String email, String phoneNumber, Role role, UserStatus status,
			LocalDateTime createdAt) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.status = status;
		this.createdAt = createdAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "UserResponse [id=" + id + ", fullName=" + fullName + ", email=" + email + ", phoneNumber=" + phoneNumber
				+ ", role=" + role + ", status=" + status + ", createdAt=" + createdAt + "]";
	}
    

}