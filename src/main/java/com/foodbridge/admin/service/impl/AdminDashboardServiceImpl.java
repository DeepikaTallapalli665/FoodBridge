
package com.foodbridge.admin.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.foodbridge.admin.service.AdminDashboardService;
import com.foodbridge.common.enums.DonationStatus;
import com.foodbridge.common.enums.Role;
import com.foodbridge.donation.entity.Donation;
import com.foodbridge.donation.repository.DonationRepository;
import com.foodbridge.user.entity.User;
import com.foodbridge.user.repository.UserRepository;

@Service
public class AdminDashboardServiceImpl
        implements AdminDashboardService {

    private final UserRepository userRepository;
    private final DonationRepository donationRepository;

    public AdminDashboardServiceImpl(
            UserRepository userRepository,
            DonationRepository donationRepository) {

        this.userRepository = userRepository;
        this.donationRepository = donationRepository;
    }

    @Override
    public Map<String, Long> getDashboardStatistics() {

        List<User> users = userRepository.findAll();
        List<Donation> donations = donationRepository.findAll();

        Map<String, Long> statistics = new LinkedHashMap<>();

        statistics.put("totalUsers", (long) users.size());

        statistics.put("totalDonors",
                users.stream()
                        .filter(user -> user.getRole() == Role.DONOR)
                        .count());

        statistics.put("totalReceivers",
                users.stream()
                        .filter(user -> user.getRole() == Role.RECEIVER)
                        .count());

        statistics.put("totalNgos",
                users.stream()
                        .filter(user -> user.getRole() == Role.NGO)
                        .count());

        statistics.put("totalAdmins",
                users.stream()
                        .filter(user -> user.getRole() == Role.ADMIN)
                        .count());

        statistics.put("totalDonations", (long) donations.size());

        statistics.put("availableDonations",
                countDonationsByStatus(donations, DonationStatus.AVAILABLE));

        statistics.put("claimedDonations",
                countDonationsByStatus(donations, DonationStatus.CLAIMED));

        statistics.put("completedDonations",
                countDonationsByStatus(donations, DonationStatus.COMPLETED));

        statistics.put("cancelledDonations",
                countDonationsByStatus(donations, DonationStatus.CANCELLED));

        statistics.put("expiredDonations",
                countDonationsByStatus(donations, DonationStatus.EXPIRED));

        return statistics;
    }

    private long countDonationsByStatus(
            List<Donation> donations,
            DonationStatus status) {

        return donations.stream()
                .filter(donation -> donation.getStatus() == status)
                .count();
    }
}
