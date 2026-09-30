package com.example.dfbbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "forms")
@Getter
@Setter
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(
            mappedBy = "form",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("displayOrder ASC")
    private List<FormField> fields = new ArrayList<>();
}