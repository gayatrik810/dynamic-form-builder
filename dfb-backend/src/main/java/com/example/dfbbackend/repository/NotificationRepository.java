package com.example.dfbbackend.repository;

import com.example.dfbbackend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification>
    findByRecipient_IdOrderByCreatedAtDesc(
            Long recipientId
    );

    List<Notification>
    findByRecipient_IdAndReadFalseOrderByCreatedAtDesc(
            Long recipientId
    );

    long countByRecipient_IdAndReadFalse(
            Long recipientId
    );
}