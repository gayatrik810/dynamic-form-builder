-- =========================================================
-- V5 - FORM FIELD TO VISIT TYPE MAPPING
-- =========================================================

CREATE TABLE form_field_visit_types
(
    form_field_id BIGINT NOT NULL,
    visit_type_id BIGINT NOT NULL,

    PRIMARY KEY (form_field_id, visit_type_id),

    CONSTRAINT fk_ffvt_form_field
        FOREIGN KEY (form_field_id)
            REFERENCES form_fields (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_ffvt_visit_type
        FOREIGN KEY (visit_type_id)
            REFERENCES visit_types (id)
            ON DELETE CASCADE
);