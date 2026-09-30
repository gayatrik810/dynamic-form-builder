package com.example.dfbbackend.service;

import com.example.dfbbackend.dto.NotificationResponse;
import com.example.dfbbackend.entity.Notification;
import com.example.dfbbackend.entity.User;
import com.example.dfbbackend.entity.UserRole;
import com.example.dfbbackend.repository.NotificationRepository;
import com.example.dfbbackend.repository.UserRepository;
import com.example.dfbbackend.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    // =========================================================
    // NOTIFY ONE USER
    // =========================================================

    @Transactional
    public void notifyUser(
            User recipient,
            User actor,
            String type,
            String title,
            String message,
            String entityType,
            Long entityId
    ) {

        if (recipient == null ||
                !Boolean.TRUE.equals(recipient.getActive())) {
            return;
        }

        Notification notification = new Notification();

        notification.setRecipient(recipient);
        notification.setActor(actor);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setEntityType(entityType);
        notification.setEntityId(entityId);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    // =========================================================
    // NOTIFY ALL ACTIVE ADMINS
    // =========================================================

    @Transactional
    public void notifyAdmins(
            User actor,
            String type,
            String title,
            String message,
            String entityType,
            Long entityId
    ) {

        List<User> users =
                userRepository.findAll();

        for (User user : users) {

            if (!Boolean.TRUE.equals(user.getActive())) {
                continue;
            }

            List<UserRole> roles =
                    userRoleRepository.findByUserId(
                            user.getId()
                    );

            boolean isAdmin =
                    roles.stream()
                            .anyMatch(userRole ->
                                    "ADMIN".equalsIgnoreCase(
                                            userRole
                                                    .getRole()
                                                    .getName()
                                    )
                            );

            if (isAdmin) {

                notifyUser(
                        user,
                        actor,
                        type,
                        title,
                        message,
                        entityType,
                        entityId
                );
            }
        }
    }

    // =========================================================
    // NOTIFY USERS OF A DOMAIN
    // =========================================================

    @Transactional
    public void notifyDomainUsers(
            Long domainId,
            User actor,
            String type,
            String title,
            String message,
            String entityType,
            Long entityId
    ) {

        if (domainId == null) {
            return;
        }

        List<User> users =
                userRepository.findAll();

        for (User user : users) {

            if (!Boolean.TRUE.equals(user.getActive())) {
                continue;
            }

            if (user.getDomain() == null) {
                continue;
            }

            if (!domainId.equals(
                    user.getDomain().getId()
            )) {
                continue;
            }

            notifyUser(
                    user,
                    actor,
                    type,
                    title,
                    message,
                    entityType,
                    entityId
            );
        }
    }

    // =========================================================
    // GET MY NOTIFICATIONS
    // =========================================================

    @Transactional(readOnly = true)
    public List<NotificationResponse> getMyNotifications(
            User user
    ) {

        return notificationRepository
                .findByRecipient_IdOrderByCreatedAtDesc(
                        user.getId()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================================================
    // GET UNREAD COUNT
    // =========================================================

    @Transactional(readOnly = true)
    public long getUnreadCount(User user) {

        return notificationRepository
                .countByRecipient_IdAndReadFalse(
                        user.getId()
                );
    }

    // =========================================================
    // MARK ONE AS READ
    // =========================================================

    @Transactional
    public void markAsRead(
            Long notificationId,
            User user
    ) {

        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"
                                )
                        );

        if (!notification
                .getRecipient()
                .getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "You cannot update this notification"
            );
        }

        notification.setRead(true);

        notificationRepository.save(notification);
    }

    // =========================================================
    // MARK ALL AS READ
    // =========================================================

    @Transactional
    public void markAllAsRead(User user) {

        List<Notification> notifications =
                notificationRepository
                        .findByRecipient_IdAndReadFalseOrderByCreatedAtDesc(
                                user.getId()
                        );

        for (Notification notification : notifications) {
            notification.setRead(true);
        }

        notificationRepository.saveAll(
                notifications
        );
    }

    // =========================================================
    // CONVERT TO RESPONSE
    // =========================================================

    private NotificationResponse toResponse(
            Notification notification
    ) {

        User actor =
                notification.getActor();

        return new NotificationResponse(

                notification.getId(),

                notification.getType(),

                notification.getTitle(),

                notification.getMessage(),

                notification.getEntityType(),

                notification.getEntityId(),

                actor != null
                        ? actor.getId()
                        : null,

                actor != null
                        ? actor.getName()
                        : null,

                notification.getRead(),

                notification.getCreatedAt()
        );
    }
}