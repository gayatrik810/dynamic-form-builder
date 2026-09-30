CREATE TABLE visit_types (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             domain_id BIGINT NOT NULL,
                             name VARCHAR(150) NOT NULL,
                             active BOOLEAN NOT NULL DEFAULT TRUE,
                             created_at DATETIME NOT NULL,
                             updated_at DATETIME NOT NULL,

                             PRIMARY KEY (id),

                             CONSTRAINT uk_visit_type_domain_name
                                 UNIQUE (domain_id, name),

                             CONSTRAINT fk_visit_type_domain
                                 FOREIGN KEY (domain_id)
                                     REFERENCES domains(id)
);