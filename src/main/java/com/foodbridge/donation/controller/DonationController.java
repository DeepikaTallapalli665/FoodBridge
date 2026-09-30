package com.foodbridge.donation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodbridge.donation.dto.request.DonationRequest;
import com.foodbridge.donation.dto.response.DonationResponse;
import com.foodbridge.donation.service.DonationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @GetMapping
    public ResponseEntity<List<DonationResponse>> getAvailableDonations(
            @RequestParam(required = false) String foodName,
            @RequestParam(required = false) String pickupLocation) {

        List<DonationResponse> responses =
                donationService.searchDonations(
                        foodName,
                        pickupLocation);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/my")
    public ResponseEntity<List<DonationResponse>> getMyDonations(
            Authentication authentication) {

        String email = authentication.getName();

        List<DonationResponse> responses =
                donationService.getMyDonations(email);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/claimed")
    public ResponseEntity<List<DonationResponse>> getClaimedDonations(
            Authentication authentication) {

        String email = authentication.getName();

        List<DonationResponse> responses =
                donationService.getClaimedDonations(email);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<DonationResponse>> getCompletedDonations(
            Authentication authentication) {

        String email = authentication.getName();

        List<DonationResponse> responses =
                donationService.getCompletedDonations(email);

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<DonationResponse> createDonation(
            @Valid @RequestBody DonationRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        DonationResponse response =
                donationService.createDonation(
                        request,
                        email);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{donationId}/accept")
    public ResponseEntity<DonationResponse> acceptDonation(
            @PathVariable Long donationId,
            Authentication authentication) {

        String email = authentication.getName();

        DonationResponse response =
                donationService.acceptDonation(
                        donationId,
                        email);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{donationId}/complete")
    public ResponseEntity<DonationResponse> completeDonation(
            @PathVariable Long donationId,
            Authentication authentication) {

        String email = authentication.getName();

        DonationResponse response =
                donationService.completeDonation(
                        donationId,
                        email);

        return ResponseEntity.ok(response);
    }
}