package com.foodbridge.user.dto.request;
import com.foodbridge.common.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

	public class UserRegistrationRequest {
		@NotBlank(message = "Full name is required")
		@Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
	    private String fullName;
		@NotBlank(message = "Email is required")
		@Email(message = "Invalid email format")
	    private String email;
		@NotBlank(message = "Phone number is required")
		@Pattern(
		    regexp = "^[6-9]\\d{9}$",
		    message = "Phone number must be a valid 10-digit Indian mobile number"
		)
		private String phoneNumber;
		@NotBlank(message = "Password is required")
		@Size(min = 8, message = "Password must contain at least 8 characters")
	    private String password;
		@NotNull(message = "Role is required")
	    private Role role;
	    public UserRegistrationRequest() {
			super();
		}
		public UserRegistrationRequest(String fullName, String email, String phoneNumber, String password, Role role) {
			super();
			this.fullName = fullName;
			this.email = email;
			this.phoneNumber = phoneNumber;
			this.password = password;
			this.role = role;
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
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public Role getRole() {
			return role;
		}
		public void setRole(Role role) {
			this.role = role;
		}
		@Override
		public String toString() {
			return "UserRegistrationRequest [fullName=" + fullName + ", email=" + email + ", phoneNumber=" + phoneNumber
					+ ", role=" + role + "]";
		}
	    

	}


