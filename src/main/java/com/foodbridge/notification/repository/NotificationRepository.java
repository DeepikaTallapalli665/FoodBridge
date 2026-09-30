package com.foodbridge.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodbridge.notification.entity.Notification;
import com.foodbridge.user.entity.User;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    List<Notification> findByUserAndIsReadFalseOrderByCreatedAtDesc(User user);
}