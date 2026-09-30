package com.foodbridge.auth.dto.response;

import com.foodbridge.common.enums.Role;

public class LoginResponse {
	private String token;
	private Long id;
	private String fullName;
	private String email;
	private Role role;
	public LoginResponse() {
		super();
	}
	public LoginResponse(String token, Long id, String fullName, String email, Role role) {
		super();
		this.token = token;
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.role = role;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	@Override
	public String toString() {
		return "LoginResponse [token=" + token + ", id=" + id + ", fullName=" + fullName + ", email=" + email
				+ ", role=" + role + "]";
	}
	

}
