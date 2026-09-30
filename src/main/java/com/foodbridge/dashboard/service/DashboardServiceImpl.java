package com.foodbridge.dashboard.service;

import org.springframework.stereotype.Service;

import com.foodbridge.common.enums.DonationStatus;
import com.foodbridge.dashboard.dto.response.DashboardResponse;
import com.foodbridge.donation.repository.DonationRepository;
import com.foodbridge.exception.ResourceNotFoundException;
import com.foodbridge.user.entity.User;
import com.foodbridge.user.repository.UserRepository;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final DonationRepository donationRepository;
    private final UserRepository userRepository;

    public DashboardServiceImpl(
            DonationRepository donationRepository,
            UserRepository userRepository) {

        this.donationRepository = donationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DashboardResponse getDashboard(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        long availableDonations =
                donationRepository.countByStatus(
                        DonationStatus.AVAILABLE);

        long myDonations = 0;
        long claimedDonations = 0;
        long completedDonations = 0;

        if (user.getRole().name().equals("DONOR")) {

            myDonations =
                    donationRepository.countByDonor(user);

        } else if (user.getRole().name().equals("RECEIVER")
                || user.getRole().name().equals("NGO")) {

            claimedDonations =
                    donationRepository.countByRecipientAndStatus(
                            user,
                            DonationStatus.CLAIMED);

            completedDonations =
                    donationRepository.countByRecipientAndStatus(
                            user,
                            DonationStatus.COMPLETED);
        }

        return new DashboardResponse(
                availableDonations,
                myDonations,
                claimedDonations,
                completedDonations);
    }
}