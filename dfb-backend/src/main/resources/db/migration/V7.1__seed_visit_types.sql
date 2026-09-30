-- =========================================================
-- V7.1 - SEED VISIT TYPES
-- =========================================================

-- =========================================================
-- AGRO
-- =========================================================

INSERT INTO visit_types
(
    domain_id,
    name,
    active,
    created_at,
    updated_at
)
SELECT
    d.id,
    'Dealer Visit',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM domains d
WHERE d.code = 'AGRO';

INSERT INTO visit_types
(
    domain_id,
    name,
    active,
    created_at,
    updated_at
)
SELECT
    d.id,
    'Farmer Visit',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM domains d
WHERE d.code = 'AGRO';

INSERT INTO visit_types
(
    domain_id,
    name,
    active,
    created_at,
    updated_at
)
SELECT
    d.id,
    'Nursery Visit',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM domains d
WHERE d.code = 'AGRO';


-- =========================================================
-- POULTRY
-- =========================================================

INSERT INTO visit_types
(
    domain_id,
    name,
    active,
    created_at,
    updated_at
)
SELECT
    d.id,
    'Branch',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM domains d
WHERE d.code = 'POULTRY';

INSERT INTO visit_types
(
    domain_id,
    name,
    active,
    created_at,
    updated_at
)
SELECT
    d.id,
    'Branch Person',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM domains d
WHERE d.code = 'POULTRY';

INSERT INTO visit_types
(
    domain_id,
    name,
    active,
    created_at,
    updated_at
)
SELECT
    d.id,
    'Doctor',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM domains d
WHERE d.code = 'POULTRY';

INSERT INTO visit_types
(
    domain_id,
    name,
    active,
    created_at,
    updated_at
)
SELECT
    d.id,
    'Corporate Office',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM domains d
WHERE d.code = 'POULTRY';

INSERT INTO visit_types
(
    domain_id,
    name,
    active,
    created_at,
    updated_at
)
SELECT
    d.id,
    'Corporate Office Person',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM domains d
WHERE d.code = 'POULTRY';

INSERT INTO visit_types
(
    domain_id,
    name,
    active,
    created_at,
    updated_at
)
SELECT
    d.id,
    'Dealer',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM domains d
WHERE d.code = 'POULTRY';

INSERT INTO visit_types
(
    domain_id,
    name,
    active,
    created_at,
    updated_at
)
SELECT
    d.id,
    'Poultry Farmer',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM domains d
WHERE d.code = 'POULTRY';


-- =========================================================
-- FMCG
-- =========================================================

INSERT INTO visit_types
(
    domain_id,
    name,
    active,
    created_at,
    updated_at
)
SELECT
    d.id,
    'Dealer',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM domains d
WHERE d.code = 'FMCG';

INSERT INTO visit_types
(
    domain_id,
    name,
    active,
    created_at,
    updated_at
)
SELECT
    d.id,
    'Corporate',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM domains d
WHERE d.code = 'FMCG';

INSERT INTO visit_types
(
    domain_id,
    name,
    active,
    created_at,
    updated_at
)
SELECT
    d.id,
    'Corporate Person',
    TRUE,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
FROM domains d
WHERE d.code = 'FMCG';

-- =========================================================
-- END V7.1
-- =========================================================