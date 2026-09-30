
package com.foodbridge.donation.service;

import java.util.List;

import com.foodbridge.donation.dto.request.DonationRequest;
import com.foodbridge.donation.dto.response.DonationResponse;

public interface DonationService {

    DonationResponse createDonation(
            DonationRequest request,
            String email
    );

    List<DonationResponse> getAvailableDonations();

    DonationResponse acceptDonation(
            Long donationId,
            String email
    );

    DonationResponse completeDonation(
            Long donationId,
            String email
    );
    List<DonationResponse> searchDonations(
            String foodName,
            String pickupLocation);

    // Donation history
    List<DonationResponse> getMyDonations(String email);

    List<DonationResponse> getClaimedDonations(String email);

    List<DonationResponse> getCompletedDonations(String email);
}
