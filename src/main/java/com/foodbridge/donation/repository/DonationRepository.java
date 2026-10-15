
package com.foodbridge.donation.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
            SELECT d FROM Donation d
            WHERE d.status = :status
              AND (
                  :#{#foodName == null} = true
                  OR LOWER(d.foodName) LIKE
                     LOWER(CONCAT('%', :foodName, '%'))
              )
              AND (
                  :#{#pickupLocation == null} = true
                  OR LOWER(d.pickupLocation) LIKE
                     LOWER(CONCAT('%', :pickupLocation, '%'))
              )
              AND (
                  :#{#expiryFrom == null} = true
                  OR d.expiryTime >= :expiryFrom
              )
              AND (
                  :#{#expiryTo == null} = true
                  OR d.expiryTime <= :expiryTo
              )
            """)
    List<Donation> searchDonations(
            @Param("status") DonationStatus status,
            @Param("foodName") String foodName,
            @Param("pickupLocation") String pickupLocation,
            @Param("expiryFrom") LocalDateTime expiryFrom,
            @Param("expiryTo") LocalDateTime expiryTo);

    long countByStatus(DonationStatus status);

    long countByDonor(User donor);

    long countByRecipientAndStatus(
            User recipient,
            DonationStatus status);

    List<Donation> findByStatusAndExpiryTimeBefore(
            DonationStatus status,
            LocalDateTime expiryTime);
}
