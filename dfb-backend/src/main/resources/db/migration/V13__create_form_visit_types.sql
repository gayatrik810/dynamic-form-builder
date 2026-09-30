CREATE TABLE form_visit_types (
                                  form_id BIGINT NOT NULL,
                                  visit_type_id BIGINT NOT NULL,

                                  PRIMARY KEY (form_id, visit_type_id),

                                  CONSTRAINT fk_form_visit_types_form
                                      FOREIGN KEY (form_id)
                                          REFERENCES forms(id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT fk_form_visit_types_visit_type
                                      FOREIGN KEY (visit_type_id)
                                          REFERENCES visit_types(id)
                                          ON DELETE CASCADE
);

CREATE INDEX idx_form_visit_types_visit_type
    ON form_visit_types (visit_type_id);