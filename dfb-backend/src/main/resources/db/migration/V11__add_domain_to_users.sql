ALTER TABLE users
    ADD COLUMN domain_id BIGINT NULL;

ALTER TABLE users
    ADD CONSTRAINT fk_users_domain
        FOREIGN KEY (domain_id)
            REFERENCES domains(id);