package com.foodbridge.donation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodbridge.common.enums.DonationStatus;
import com.foodbridge.donation.entity.Donation;
import com.foodbridge.user.entity.User;

public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByStatus(DonationStatus status);

    List<Donation> findByDonor(User donor);

    List<Donation> findByRecipient(User recipient);

    List<Donation> findByStatusAndFoodNameContainingIgnoreCase(
            DonationStatus status,
            String foodName);

    List<Donation> findByStatusAndPickupLocationContainingIgnoreCase(
            DonationStatus status,
            String pickupLocation);
    List<Donation> findByStatusAndFoodNameContainingIgnoreCaseAndPickupLocationContainingIgnoreCase(
            DonationStatus status,
            String foodName,
            String pickupLocation);
    long countByStatus(DonationStatus status);

    long countByDonor(User donor);

    long countByRecipientAndStatus(
            User recipient,
            DonationStatus status);
}