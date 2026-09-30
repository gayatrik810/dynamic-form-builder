package com.example.dfbbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "form_fields",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_form_field_key",
                        columnNames = {"form_id", "field_key"}
                )
        }
)
@Getter
@Setter
public class FormField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Column(name = "field_key", nullable = false, length = 100)
    private String fieldKey;

    @Column(nullable = false, length = 150)
    private String label;

    @Column(name = "field_type", nullable = false, length = 50)
    private String fieldType;

    @Column(nullable = false)
    private Boolean required = false;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    @Column(length = 255)
    private String placeholder;

    @Column(name = "validation_rule", length = 500)
    private String validationRule;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(
            mappedBy = "field",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("displayOrder ASC")
    private List<FieldOption> options = new ArrayList<>();
}