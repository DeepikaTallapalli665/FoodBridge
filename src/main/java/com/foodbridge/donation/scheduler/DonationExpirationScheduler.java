package com.foodbridge.donation.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.foodbridge.common.enums.DonationStatus;
import com.foodbridge.common.enums.NotificationType;
import com.foodbridge.donation.entity.Donation;
import com.foodbridge.donation.repository.DonationRepository;
import com.foodbridge.notification.service.NotificationService;

@Component
public class DonationExpirationScheduler {

    private final DonationRepository donationRepository;
    private final NotificationService notificationService;

    public DonationExpirationScheduler(
            DonationRepository donationRepository,
            NotificationService notificationService) {

        this.donationRepository = donationRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRate = 60000)
    public void expireDonations() {

        LocalDateTime now = LocalDateTime.now();

        List<Donation> donations =
                donationRepository.findByStatus(
                        DonationStatus.AVAILABLE);

        for (Donation donation : donations) {

            if (!donation.getExpiryTime().isAfter(now)) {

                donation.setStatus(DonationStatus.EXPIRED);

                donationRepository.save(donation);

                notificationService.createNotification(
                        donation.getDonor().getId(),
                        "Your donation has expired",
                        NotificationType.DONATION_EXPIRED
                );
            }
        }
    }
}