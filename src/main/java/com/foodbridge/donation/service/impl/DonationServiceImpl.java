package com.foodbridge.donation.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.foodbridge.common.enums.DonationStatus;
import com.foodbridge.common.enums.NotificationType;
import com.foodbridge.common.enums.Role;
import com.foodbridge.donation.dto.request.DonationRequest;
import com.foodbridge.donation.dto.response.DonationResponse;
import com.foodbridge.donation.entity.Donation;
import com.foodbridge.donation.repository.DonationRepository;
import com.foodbridge.donation.service.DonationService;
import com.foodbridge.exception.BadRequestException;
import com.foodbridge.exception.ResourceNotFoundException;
import com.foodbridge.notification.service.NotificationService;
import com.foodbridge.user.entity.User;
import com.foodbridge.user.exception.InvalidRoleException;
import com.foodbridge.user.repository.UserRepository;

@Service
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public DonationServiceImpl(
            DonationRepository donationRepository,
            UserRepository userRepository,
            NotificationService notificationService) {

        this.donationRepository = donationRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    // =========================================================
    // CREATE DONATION
    // =========================================================

    @Override
    public DonationResponse createDonation(
            DonationRequest request,
            String email) {

        User donor = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Donor not found"));

        Donation donation = new Donation();

        donation.setFoodName(request.getFoodName());
        donation.setDescription(request.getDescription());
        donation.setQuantity(request.getQuantity());
        donation.setQuantityUnit(request.getQuantityUnit());
        donation.setPickupLocation(request.getPickupLocation());
        donation.setExpiryTime(request.getExpiryTime());
        donation.setDonor(donor);
        donation.setStatus(DonationStatus.AVAILABLE);
        donation.setCreatedAt(LocalDateTime.now());

        donation = donationRepository.save(donation);

        return mapToResponse(donation);
    }

    // =========================================================
    // GET AVAILABLE DONATIONS
    // =========================================================

    @Override
    public List<DonationResponse> getAvailableDonations() {

        List<Donation> donations =
                donationRepository.findByStatus(
                        DonationStatus.AVAILABLE);

        List<DonationResponse> responses =
                new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (Donation donation : donations) {

            // Check if donation has expired
            if (!donation.getExpiryTime().isAfter(now)) {

                donation.setStatus(DonationStatus.EXPIRED);

                donationRepository.save(donation);

                notificationService.createNotification(
                        donation.getDonor().getId(),
                        "Your donation has expired",
                        NotificationType.DONATION_EXPIRED
                );

                continue;
            }

            responses.add(mapToResponse(donation));
        }

        return responses;
    }

    // =========================================================
    // ACCEPT / CLAIM DONATION
    // =========================================================

    @Override
    public DonationResponse acceptDonation(
            Long donationId,
            String email) {

        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Donation not found"));

        User recipient = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Recipient not found"));

        if (recipient.getRole() != Role.RECEIVER &&
            recipient.getRole() != Role.NGO) {

            throw new InvalidRoleException(
                    "Only RECEIVER or NGO can claim a donation");
        }

        if (donation.getDonor().getId()
                .equals(recipient.getId())) {

            throw new BadRequestException(
                    "Donor cannot claim their own donation");
        }

        if (donation.getStatus() != DonationStatus.AVAILABLE) {

            throw new BadRequestException(
                    "Donation is no longer available");
        }

        // Check expiry before claiming
        if (!donation.getExpiryTime()
                .isAfter(LocalDateTime.now())) {

            donation.setStatus(DonationStatus.EXPIRED);

            donationRepository.save(donation);

            notificationService.createNotification(
                    donation.getDonor().getId(),
                    "Your donation has expired",
                    NotificationType.DONATION_EXPIRED
            );

            throw new BadRequestException(
                    "Donation has expired");
        }

        donation.setRecipient(recipient);
        donation.setStatus(DonationStatus.CLAIMED);

        donation = donationRepository.save(donation);

        // Notify donor that the donation was claimed
        notificationService.createNotification(
                donation.getDonor().getId(),
                "Your donation has been claimed",
                NotificationType.DONATION_CLAIMED
        );

        return mapToResponse(donation);
    }
    @Override
    public List<DonationResponse> searchDonations(
            String foodName,
            String pickupLocation) {

        List<Donation> donations;

        if (foodName != null && !foodName.isBlank()
                && pickupLocation != null && !pickupLocation.isBlank()) {

            donations = donationRepository
                    .findByStatusAndFoodNameContainingIgnoreCaseAndPickupLocationContainingIgnoreCase(
                            DonationStatus.AVAILABLE,
                            foodName,
                            pickupLocation);

        } else if (foodName != null && !foodName.isBlank()) {

            donations = donationRepository
                    .findByStatusAndFoodNameContainingIgnoreCase(
                            DonationStatus.AVAILABLE,
                            foodName);

        } else if (pickupLocation != null && !pickupLocation.isBlank()) {

            donations = donationRepository
                    .findByStatusAndPickupLocationContainingIgnoreCase(
                            DonationStatus.AVAILABLE,
                            pickupLocation);

        } else {

            donations = donationRepository
                    .findByStatus(DonationStatus.AVAILABLE);
        }

        List<DonationResponse> responses = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (Donation donation : donations) {

            if (!donation.getExpiryTime().isAfter(now)) {

                donation.setStatus(DonationStatus.EXPIRED);

                donationRepository.save(donation);

                notificationService.createNotification(
                        donation.getDonor().getId(),
                        "Your donation has expired",
                        NotificationType.DONATION_EXPIRED
                );

                continue;
            }

            responses.add(mapToResponse(donation));
        }

        return responses;
    }    // =========================================================
    // COMPLETE DONATION
    // =========================================================

    @Override
    public DonationResponse completeDonation(
            Long donationId,
            String email) {

        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Donation not found"));

        User recipient = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Recipient not found"));

        if (donation.getStatus() != DonationStatus.CLAIMED) {

            throw new BadRequestException(
                    "Only claimed donations can be completed");
        }

        if (donation.getRecipient() == null ||
            !donation.getRecipient().getId()
                    .equals(recipient.getId())) {

            throw new BadRequestException(
                    "Only the recipient who claimed this donation "
                    + "can complete it");
        }

        donation.setStatus(DonationStatus.COMPLETED);

        donation = donationRepository.save(donation);

        // Notify donor that the donation was completed
        notificationService.createNotification(
                donation.getDonor().getId(),
                "Your donation has been completed",
                NotificationType.DONATION_COMPLETED
        );

        return mapToResponse(donation);
    }

    // =========================================================
    // DONOR HISTORY
    // =========================================================

    @Override
    public List<DonationResponse> getMyDonations(String email) {

        User donor = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Donor not found"));

        List<Donation> donations =
                donationRepository.findByDonor(donor);

        List<DonationResponse> responses =
                new ArrayList<>();

        for (Donation donation : donations) {
            responses.add(mapToResponse(donation));
        }

        return responses;
    }

    // =========================================================
    // CLAIMED DONATIONS
    // =========================================================

    @Override
    public List<DonationResponse> getClaimedDonations(
            String email) {

        User recipient = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Recipient not found"));

        List<Donation> donations =
                donationRepository.findByRecipient(recipient);

        List<DonationResponse> responses =
                new ArrayList<>();

        for (Donation donation : donations) {

            if (donation.getStatus() ==
                    DonationStatus.CLAIMED) {

                responses.add(mapToResponse(donation));
            }
        }

        return responses;
    }

    // =========================================================
    // COMPLETED DONATIONS
    // =========================================================

    @Override
    public List<DonationResponse> getCompletedDonations(
            String email) {

        User recipient = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Recipient not found"));

        List<Donation> donations =
                donationRepository.findByRecipient(recipient);

        List<DonationResponse> responses =
                new ArrayList<>();

        for (Donation donation : donations) {

            if (donation.getStatus() ==
                    DonationStatus.COMPLETED) {

                responses.add(mapToResponse(donation));
            }
        }

        return responses;
    }

    // =========================================================
    // DONATION → RESPONSE MAPPER
    // =========================================================

    private DonationResponse mapToResponse(
            Donation donation) {

        DonationResponse response =
                new DonationResponse();

        response.setId(donation.getId());
        response.setFoodName(donation.getFoodName());
        response.setDescription(donation.getDescription());
        response.setQuantity(donation.getQuantity());
        response.setQuantityUnit(donation.getQuantityUnit());
        response.setPickupLocation(donation.getPickupLocation());
        response.setExpiryTime(donation.getExpiryTime());
        response.setStatus(donation.getStatus());

        if (donation.getDonor() != null) {
            response.setDonorId(
                    donation.getDonor().getId());
        }

        if (donation.getRecipient() != null) {
            response.setRecipientId(
                    donation.getRecipient().getId());
        }

        response.setCreatedAt(donation.getCreatedAt());

        return response;
    }
}