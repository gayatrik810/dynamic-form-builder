ALTER TABLE users
    ADD COLUMN phone_number VARCHAR(10);

ALTER TABLE users
    ADD CONSTRAINT chk_users_phone_number
        CHECK (
            phone_number IS NULL
                OR phone_number REGEXP '^[0-9]{10}$'
            );