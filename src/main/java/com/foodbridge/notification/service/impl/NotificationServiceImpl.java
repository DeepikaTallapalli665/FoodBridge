package com.foodbridge.notification.service.impl;

import java.time.LocalDateTime;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.foodbridge.common.enums.NotificationType;
import com.foodbridge.exception.BadRequestException;
import com.foodbridge.exception.ResourceNotFoundException;
import com.foodbridge.notification.dto.response.NotificationResponse;
import com.foodbridge.notification.entity.Notification;
import com.foodbridge.notification.repository.NotificationRepository;
import com.foodbridge.notification.service.NotificationService;
import com.foodbridge.user.entity.User;
import com.foodbridge.user.repository.UserRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationServiceImpl(
            UserRepository userRepository,
            NotificationRepository notificationRepository,
            SimpMessagingTemplate messagingTemplate) {

        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void createNotification(
            Long userId,
            String message,
            NotificationType type) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);

        messagingTemplate.convertAndSend(
                "/queue/notifications/" + userId,
                notification);
    }

    @Override
    public List<NotificationResponse> getMyNotifications(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        List<Notification> notifications =
                notificationRepository.findByUserOrderByCreatedAtDesc(user);

        List<NotificationResponse> responses = new ArrayList<>();

        for (Notification notification : notifications) {
            responses.add(mapToResponse(notification));
        }

        return responses;
    }

    @Override
    public List<NotificationResponse> getMyUnreadNotifications(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        List<Notification> notifications =
                notificationRepository
                        .findByUserAndIsReadFalseOrderByCreatedAtDesc(user);

        List<NotificationResponse> responses = new ArrayList<>();

        for (Notification notification : notifications) {
            responses.add(mapToResponse(notification));
        }

        return responses;
    }

    @Override
    public void markAsRead(Long notificationId, String email) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Notification not found"));

        if (!notification.getUser().getEmail().equals(email)) {
            throw new BadRequestException(
                    "You cannot modify another user's notification");
        }

        notification.setRead(true);

        notificationRepository.save(notification);
    }

    private NotificationResponse mapToResponse(
            Notification notification) {

        NotificationResponse response = new NotificationResponse();

        response.setId(notification.getId());
        response.setMessage(notification.getMessage());
        response.setType(notification.getType());
        response.setRead(notification.isRead());
        response.setCreatedAt(notification.getCreatedAt());

        return response;
    }
}