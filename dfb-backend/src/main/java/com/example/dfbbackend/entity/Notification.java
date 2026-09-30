package com.example.dfbbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "recipient_id",
            nullable = false
    )
    private User recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id")
    private User actor;

    @Column(
            nullable = false,
            length = 50
    )
    private String type;

    @Column(
            nullable = false,
            length = 150
    )
    private String title;

    @Column(
            nullable = false,
            length = 500
    )
    private String message;

    @Column(
            name = "entity_type",
            length = 50
    )
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(
            name = "is_read",
            nullable = false
    )
    private Boolean read = false;

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;
}