package com.example.dfbbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "submission_values",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_submission_field",
                        columnNames = {"submission_id", "field_id"}
                )
        }
)
@Getter
@Setter
public class SubmissionValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private FormSubmission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private FormField field;

    @Column(name = "value_string", columnDefinition = "TEXT")
    private String valueString;

    @Column(name = "value_number", precision = 20, scale = 6)
    private BigDecimal valueNumber;

    @Column(name = "value_boolean")
    private Boolean valueBoolean;

    @Column(name = "value_date")
    private LocalDate valueDate;

    @Column(name = "value_datetime")
    private LocalDateTime valueDatetime;
}