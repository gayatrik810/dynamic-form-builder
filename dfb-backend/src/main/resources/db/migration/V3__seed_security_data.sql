-- =========================================================
-- V3 - SEED DOMAINS + SECURITY DATA
-- =========================================================


-- =========================================================
-- 1. DOMAINS
-- AGRO / POULTRY / FMCG
-- =========================================================

INSERT INTO domains
(
    name,
    code,
    description,
    active
)
VALUES
    (
        'Agro',
        'AGRO',
        'Agro domain',
        TRUE
    ),
    (
        'Poultry',
        'POULTRY',
        'Poultry domain',
        TRUE
    ),
    (
        'FMCG',
        'FMCG',
        'FMCG domain',
        TRUE
    );


-- =========================================================
-- 2. ROLES
-- =========================================================

INSERT INTO roles (name)
VALUES ('ADMIN'),
       ('USER');


-- =========================================================
-- 3. INITIAL ADMIN USER
-- Password: Test@123
-- Replace the password value with your BCrypt hash
-- =========================================================

INSERT INTO users
(
    name,
    email,
    password,
    active
)
VALUES
    (
        'Gayatri',
        'gayatri@test.com',
        'YOUR_BCRYPT_HASH_HERE',
        TRUE
    );


-- =========================================================
-- 4. ASSIGN ADMIN ROLE TO GAYATRI
-- =========================================================

INSERT INTO user_roles
(
    user_id,
    role_id
)
SELECT
    u.id,
    r.id
FROM users u
         JOIN roles r
              ON r.name = 'ADMIN'
WHERE u.email = 'gayatri@test.com';