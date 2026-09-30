package com.foodbridge.donation.dto.response;

import java.time.LocalDateTime;

import com.foodbridge.common.enums.DonationStatus;

public class DonationResponse {

    private Long id;
    private String foodName;
    private String description;
    private Double quantity;
    private String quantityUnit;
    private String pickupLocation;
    private LocalDateTime expiryTime;
    private DonationStatus status;
    private Long donorId;
    private LocalDateTime createdAt;
    private Long recipientId;
  
    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public DonationStatus getStatus() {
        return status;
    }

    public void setStatus(DonationStatus status) {
        this.status = status;
    }

    public Long getDonorId() {
        return donorId;
    }
    public Long getRecipientId() {
        return recipientId;
    }


    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}