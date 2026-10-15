
package com.foodbridge.donation.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.foodbridge.common.enums.DonationStatus;
import com.foodbridge.donation.entity.Donation;
import com.foodbridge.donation.repository.DonationRepository;

@Component
public class DonationExpiryScheduler {

    private final DonationRepository donationRepository;

    public DonationExpiryScheduler(
            DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void expireDonations() {

        LocalDateTime now = LocalDateTime.now();

        List<Donation> expiredDonations =
                donationRepository.findByStatusAndExpiryTimeBefore(
                        DonationStatus.AVAILABLE,
                        now);

        if (expiredDonations.isEmpty()) {
            return;
        }

        for (Donation donation : expiredDonations) {
            donation.setStatus(DonationStatus.EXPIRED);
        }

        donationRepository.saveAll(expiredDonations);

        System.out.println(
                "Donation expiry scheduler: marked "
                + expiredDonations.size()
                + " donation(s) as EXPIRED.");
    }
}
