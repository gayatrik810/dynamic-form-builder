package com.example.dfbbackend.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class FormFieldVisitTypeId implements Serializable {

    private Long formFieldId;
    private Long visitTypeId;

    public FormFieldVisitTypeId() {
    }

    public FormFieldVisitTypeId(Long formFieldId, Long visitTypeId) {
        this.formFieldId = formFieldId;
        this.visitTypeId = visitTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof FormFieldVisitTypeId that)) {
            return false;
        }

        return Objects.equals(formFieldId, that.formFieldId)
                && Objects.equals(visitTypeId, that.visitTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formFieldId, visitTypeId);
    }
}