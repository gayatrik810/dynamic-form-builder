ALTER TABLE form_submissions
    ADD COLUMN visit_type_id BIGINT NULL;

ALTER TABLE form_submissions
    ADD CONSTRAINT fk_form_submissions_visit_type
        FOREIGN KEY (visit_type_id)
            REFERENCES visit_types(id);