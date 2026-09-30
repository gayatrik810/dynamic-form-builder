package com.example.dfbbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "visit_types",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_visit_type_domain_name",
                        columnNames = {"domain_id", "name"}
                )
        }
)
@Getter
@Setter
public class VisitType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}