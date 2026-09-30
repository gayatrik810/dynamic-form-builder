package com.example.dfbbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NotificationResponse {

    private Long id;

    private String type;

    private String title;

    private String message;

    private String entityType;

    private Long entityId;

    private Long actorId;

    private String actorName;

    private Boolean read;

    private LocalDateTime createdAt;
}