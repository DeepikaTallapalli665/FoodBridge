package com.foodbridge.admin.service;

import java.util.List;

import com.foodbridge.common.enums.DonationStatus;
import com.foodbridge.donation.dto.response.DonationResponse;

public interface AdminDonationService {

    List<DonationResponse> getAllDonations();

    List<DonationResponse> getDonationsByStatus(
            DonationStatus status);
}