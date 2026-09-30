package com.foodbridge.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserUpdateRequest {

    @NotBlank(message = "Full name is required")
    @Size(
        min = 3,
        max = 100,
        message = "Full name must be between 3 and 100 characters"
    )
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^[6-9]\\d{9}$",
        message = "Phone number must be a valid 10-digit Indian mobile number"
    )
    private String phoneNumber;

    public UserUpdateRequest() {
        super();
    }

    public UserUpdateRequest(String fullName, String phoneNumber) {
        super();
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "UserUpdateRequest [fullName=" + fullName
                + ", phoneNumber=" + phoneNumber + "]";
    }
}