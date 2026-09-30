package com.foodbridge.notification.service;

import java.util.List;

import com.foodbridge.common.enums.NotificationType;
import com.foodbridge.notification.dto.response.NotificationResponse;

public interface NotificationService {

    void createNotification(
            Long userId,
            String message,
            NotificationType type
    );

    List<NotificationResponse> getMyNotifications(String email);

    List<NotificationResponse> getMyUnreadNotifications(String email);

    void markAsRead(Long notificationId, String email);
}