-- =========================================================
-- 1. USERS
-- =========================================================

CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(150) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    active     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);


-- =========================================================
-- 2. ROLES
-- =========================================================

CREATE TABLE roles
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);


-- =========================================================
-- 3. USER ROLES
-- =========================================================

CREATE TABLE user_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, role_id),

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id)
            REFERENCES roles (id)
            ON DELETE CASCADE
);


-- =========================================================
-- 4. DOMAINS
-- AGRO / POULTRY / FMCG
-- =========================================================

CREATE TABLE domains
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    code        VARCHAR(50)  NOT NULL UNIQUE,
    description VARCHAR(500),
    active      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);


-- =========================================================
-- 5. USER DOMAINS
-- Defines which domains a user can access
-- =========================================================

CREATE TABLE user_domains
(
    user_id   BIGINT NOT NULL,
    domain_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, domain_id),

    CONSTRAINT fk_user_domains_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_user_domains_domain
        FOREIGN KEY (domain_id)
            REFERENCES domains (id)
            ON DELETE CASCADE
);


-- =========================================================
-- 6. FORMS
-- Example:
-- AGRO -> Farmer / Dealer / Nursery
-- POULTRY -> Farmer / Dealer / Doctor / etc.
-- FMCG -> Dealer / Corporate / etc.
-- =========================================================

CREATE TABLE forms
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,

    domain_id   BIGINT       NOT NULL,

    name        VARCHAR(150) NOT NULL,
    code        VARCHAR(100) NOT NULL,
    description VARCHAR(500),

    active      BOOLEAN      NOT NULL DEFAULT TRUE,

    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uk_forms_code
        UNIQUE (code),

    CONSTRAINT fk_forms_domain
        FOREIGN KEY (domain_id)
            REFERENCES domains (id)
            ON DELETE CASCADE
);


-- =========================================================
-- 7. FORM FIELDS
-- Dynamic field definitions / EAV ATTRIBUTES
-- =========================================================

CREATE TABLE form_fields
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,

    form_id         BIGINT       NOT NULL,

    field_key       VARCHAR(100) NOT NULL,
    label           VARCHAR(150) NOT NULL,

    field_type      VARCHAR(50)  NOT NULL,

    required        BOOLEAN      NOT NULL DEFAULT FALSE,

    display_order   INT          NOT NULL DEFAULT 0,

    placeholder     VARCHAR(255),

    validation_rule VARCHAR(500),

    active          BOOLEAN      NOT NULL DEFAULT TRUE,

    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uk_form_field_key
        UNIQUE (form_id, field_key),

    CONSTRAINT fk_form_fields_form
        FOREIGN KEY (form_id)
            REFERENCES forms (id)
            ON DELETE CASCADE
);


-- =========================================================
-- 8. FIELD OPTIONS
-- For SELECT / RADIO / CHECKBOX / MULTI_SELECT
-- =========================================================

CREATE TABLE field_options
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,

    field_id      BIGINT       NOT NULL,

    label         VARCHAR(150) NOT NULL,
    value         VARCHAR(150) NOT NULL,

    display_order INT          NOT NULL DEFAULT 0,

    active        BOOLEAN      NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_field_options_field
        FOREIGN KEY (field_id)
            REFERENCES form_fields (id)
            ON DELETE CASCADE
);


-- =========================================================
-- 9. FIELD RULES
-- Conditional rendering
--
-- Example:
-- decision_maker = YES
--       ↓
-- SHOW decision_area
-- =========================================================

CREATE TABLE field_rules
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,

    form_id         BIGINT      NOT NULL,

    source_field_id BIGINT      NOT NULL,

    operator        VARCHAR(30) NOT NULL,

    expected_value  VARCHAR(255),

    target_field_id BIGINT      NOT NULL,

    action          VARCHAR(30) NOT NULL,

    CONSTRAINT fk_field_rules_form
        FOREIGN KEY (form_id)
            REFERENCES forms (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_field_rules_source
        FOREIGN KEY (source_field_id)
            REFERENCES form_fields (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_field_rules_target
        FOREIGN KEY (target_field_id)
            REFERENCES form_fields (id)
            ON DELETE CASCADE
);


-- =========================================================
-- 10. FORM SUBMISSIONS
-- EAV ENTITY
--
-- One row = one submitted form record
-- =========================================================

CREATE TABLE form_submissions
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,

    form_id      BIGINT    NOT NULL,

    submitted_by BIGINT    NOT NULL,

    submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_submissions_form
        FOREIGN KEY (form_id)
            REFERENCES forms (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_submissions_user
        FOREIGN KEY (submitted_by)
            REFERENCES users (id)
);


-- =========================================================
-- 11. SUBMISSION VALUES
-- EAV VALUES
-- =========================================================

CREATE TABLE submission_values
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,

    submission_id  BIGINT NOT NULL,

    field_id       BIGINT NOT NULL,

    value_string   TEXT,

    value_number   DECIMAL(20, 6),

    value_boolean  BOOLEAN,

    value_date     DATE,

    value_datetime DATETIME,

    CONSTRAINT fk_submission_values_submission
        FOREIGN KEY (submission_id)
            REFERENCES form_submissions (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_submission_values_field
        FOREIGN KEY (field_id)
            REFERENCES form_fields (id)
            ON DELETE CASCADE,

    CONSTRAINT uk_submission_field
        UNIQUE (submission_id, field_id)
);


-- =========================================================
-- INDEXES
-- =========================================================

CREATE INDEX idx_forms_domain
    ON forms (domain_id);

CREATE INDEX idx_form_fields_form
    ON form_fields (form_id);

CREATE INDEX idx_field_options_field
    ON field_options (field_id);

CREATE INDEX idx_field_rules_form
    ON field_rules (form_id);

CREATE INDEX idx_submissions_form
    ON form_submissions (form_id);

CREATE INDEX idx_submissions_user
    ON form_submissions (submitted_by);

CREATE INDEX idx_submission_values_submission
    ON submission_values (submission_id);

CREATE INDEX idx_submission_values_field
    ON submission_values (field_id);