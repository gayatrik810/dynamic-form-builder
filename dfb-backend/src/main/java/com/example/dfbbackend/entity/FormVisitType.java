package com.example.dfbbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "form_visit_types")
@Getter
@Setter
public class FormVisitType {

    @EmbeddedId
    private FormVisitTypeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("formId")
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("visitTypeId")
    @JoinColumn(name = "visit_type_id", nullable = false)
    private VisitType visitType;

    public FormVisitType() {
    }

    public FormVisitType(
            Form form,
            VisitType visitType
    ) {
        this.form = form;
        this.visitType = visitType;

        this.id = new FormVisitTypeId(
                form.getId(),
                visitType.getId()
        );
    }
}