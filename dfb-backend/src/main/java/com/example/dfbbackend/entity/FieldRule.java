package com.example.dfbbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "field_rules")
@Getter
@Setter
public class FieldRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_type_id", nullable = false)
    private VisitType visitType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_field_id", nullable = false)
    private FormField sourceField;

    @Column(nullable = false, length = 30)
    private String operator;

    @Column(name = "expected_value", length = 255)
    private String expectedValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_field_id", nullable = false)
    private FormField targetField;

    @Column(nullable = false, length = 30)
    private String action;
}