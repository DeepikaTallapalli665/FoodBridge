package com.foodbridge.notification.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodbridge.notification.dto.response.NotificationResponse;
import com.foodbridge.notification.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(
            Authentication authentication) {

        String email = authentication.getName();

        List<NotificationResponse> responses =
                notificationService.getMyNotifications(email);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getMyUnreadNotifications(
            Authentication authentication) {

        String email = authentication.getName();

        List<NotificationResponse> responses =
                notificationService.getMyUnreadNotifications(email);

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<String> markAsRead(
            @PathVariable Long notificationId,
            Authentication authentication) {

        String email = authentication.getName();

        notificationService.markAsRead(notificationId, email);

        return ResponseEntity.ok("Notification marked as read");
    }
}