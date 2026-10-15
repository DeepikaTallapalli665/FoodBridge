package com.foodbridge.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodbridge.admin.service.AdminDonationService;
import com.foodbridge.common.enums.DonationStatus;
import com.foodbridge.donation.dto.response.DonationResponse;

@RestController
@RequestMapping("/api/admin/donations")
public class AdminDonationController {

    private final AdminDonationService adminDonationService;

    public AdminDonationController(
            AdminDonationService adminDonationService) {

        this.adminDonationService = adminDonationService;
    }

    @GetMapping
    public ResponseEntity<List<DonationResponse>> getDonations(
            @RequestParam(required = false) DonationStatus status) {

        List<DonationResponse> donations;

        if (status == null) {
            donations =
                    adminDonationService.getAllDonations();
        } else {
            donations =
                    adminDonationService
                            .getDonationsByStatus(status);
        }

        return ResponseEntity.ok(donations);
    }
}