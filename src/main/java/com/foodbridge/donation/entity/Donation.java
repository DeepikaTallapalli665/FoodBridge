package com.foodbridge.donation.entity;

import java.time.LocalDateTime;

import com.foodbridge.common.enums.DonationStatus;
import com.foodbridge.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
@Entity
public class Donation{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String foodName;
	private String description;
	private Double quantity;
	private String quantityUnit;
    private String pickupLocation;
    private LocalDateTime expiryTime;
    
    @Enumerated(EnumType.STRING)
    private DonationStatus status;

    @ManyToOne
    private User donor;
    @ManyToOne
    private User recipient;

    private LocalDateTime createdAt;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public void setStatus(DonationStatus status) {
        this.status = status;
    }

    public void setDonor(User donor) {
        this.donor = donor;
    }
    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getFoodName() {
        return foodName;
    }

    public String getDescription() {
        return description;
    }

    public Double getQuantity() {
        return quantity;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public DonationStatus getStatus() {
        return status;
    }

    public User getDonor() {
        return donor;
    }

    public User getRecipient() {
        return recipient;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    
	
}