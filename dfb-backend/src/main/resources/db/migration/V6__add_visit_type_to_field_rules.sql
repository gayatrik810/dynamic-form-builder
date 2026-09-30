-- =========================================================
-- V6 - ADD VISIT TYPE TO FIELD RULES
-- =========================================================

ALTER TABLE field_rules
    ADD COLUMN visit_type_id BIGINT NOT NULL;

ALTER TABLE field_rules
    ADD CONSTRAINT fk_field_rules_visit_type
        FOREIGN KEY (visit_type_id)
            REFERENCES visit_types (id)
            ON DELETE CASCADE;

CREATE INDEX idx_field_rules_visit_type
    ON field_rules (visit_type_id);