package com.example.dfbbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "form_field_visit_types")
@Getter
@Setter
public class FormFieldVisitType {

    @EmbeddedId
    private FormFieldVisitTypeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("formFieldId")
    @JoinColumn(name = "form_field_id", nullable = false)
    private FormField formField;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("visitTypeId")
    @JoinColumn(name = "visit_type_id", nullable = false)
    private VisitType visitType;

    public FormFieldVisitType() {
    }

    public FormFieldVisitType(FormField formField, VisitType visitType) {
        this.formField = formField;
        this.visitType = visitType;

        this.id = new FormFieldVisitTypeId(
                formField.getId(),
                visitType.getId()
        );
    }
}