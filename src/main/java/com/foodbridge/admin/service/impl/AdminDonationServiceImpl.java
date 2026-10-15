package com.foodbridge.admin.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.foodbridge.admin.service.AdminDonationService;
import com.foodbridge.common.enums.DonationStatus;
import com.foodbridge.donation.dto.response.DonationResponse;
import com.foodbridge.donation.entity.Donation;
import com.foodbridge.donation.repository.DonationRepository;

@Service
public class AdminDonationServiceImpl implements AdminDonationService {

    private final DonationRepository donationRepository;

    public AdminDonationServiceImpl(
            DonationRepository donationRepository) {

        this.donationRepository = donationRepository;
    }

    @Override
    public List<DonationResponse> getAllDonations() {

        List<Donation> donations =
                donationRepository.findAll();

        return donations.stream()
                .map(this::mapToDonationResponse)
                .toList();
    }

    @Override
    public List<DonationResponse> getDonationsByStatus(
            DonationStatus status) {

        List<Donation> donations =
                donationRepository.findAll()
                        .stream()
                        .filter(donation ->
                                donation.getStatus() == status)
                        .toList();

        return donations.stream()
                .map(this::mapToDonationResponse)
                .toList();
    }

    private DonationResponse mapToDonationResponse(
            Donation donation) {

        DonationResponse response = new DonationResponse();

        response.setId(donation.getId());
        response.setFoodName(donation.getFoodName());
        response.setDescription(donation.getDescription());
        response.setQuantity(donation.getQuantity());
        response.setQuantityUnit(donation.getQuantityUnit());
        response.setPickupLocation(donation.getPickupLocation());
        response.setExpiryTime(donation.getExpiryTime());
        response.setStatus(donation.getStatus());
        response.setCreatedAt(donation.getCreatedAt());

        if (donation.getDonor() != null) {
            response.setDonorId(
                    donation.getDonor().getId());
        }

        if (donation.getRecipient() != null) {
            response.setRecipientId(
                    donation.getRecipient().getId());
        }

        return response;
    }
}