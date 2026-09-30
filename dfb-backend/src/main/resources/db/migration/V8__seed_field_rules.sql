-- =========================================================
-- V8 - SEED FIELD RULES
-- =========================================================

-- =========================================================
-- 1. AGRO - DEALER VISIT
-- dealerType = Other -> show otherDealerType
-- =========================================================

INSERT INTO field_rules
(
    form_id,
    visit_type_id,
    source_field_id,
    operator,
    expected_value,
    target_field_id,
    action
)
VALUES
    (
        1,
        1,
        5,
        'EQUALS',
        'Other',
        6,
        'SHOW'
    );


-- =========================================================
-- 2. AGRO - FARMER VISIT
-- crop = Other -> show otherCrop
-- =========================================================

INSERT INTO field_rules
(
    form_id,
    visit_type_id,
    source_field_id,
    operator,
    expected_value,
    target_field_id,
    action
)
VALUES
    (
        2,
        2,
        11,
        'EQUALS',
        'Other',
        12,
        'SHOW'
    );


-- =========================================================
-- 3. AGRO - NURSERY VISIT
-- nurseryType = Other -> show otherNurseryType
-- =========================================================

INSERT INTO field_rules
(
    form_id,
    visit_type_id,
    source_field_id,
    operator,
    expected_value,
    target_field_id,
    action
)
VALUES
    (
        3,
        3,
        19,
        'EQUALS',
        'Other',
        20,
        'SHOW'
    );


-- =========================================================
-- 4. POULTRY - BRANCH PERSON VISIT
-- designation = Other -> show otherDesignation
-- =========================================================

INSERT INTO field_rules
(
    form_id,
    visit_type_id,
    source_field_id,
    operator,
    expected_value,
    target_field_id,
    action
)
VALUES
    (
        5,
        5,
        38,
        'EQUALS',
        'Other',
        39,
        'SHOW'
    );


-- =========================================================
-- 5. POULTRY - CORPORATE OFFICE VISIT
-- businessType = Other -> show otherBusinessType
-- =========================================================

INSERT INTO field_rules
(
    form_id,
    visit_type_id,
    source_field_id,
    operator,
    expected_value,
    target_field_id,
    action
)
VALUES
    (
        7,
        7,
        54,
        'EQUALS',
        'Other',
        55,
        'SHOW'
    );


-- =========================================================
-- 6. POULTRY - CORPORATE OFFICE PERSON VISIT
-- decisionArea = Other -> show otherDecisionArea
-- =========================================================

INSERT INTO field_rules
(
    form_id,
    visit_type_id,
    source_field_id,
    operator,
    expected_value,
    target_field_id,
    action
)
VALUES
    (
        8,
        8,
        64,
        'EQUALS',
        'Other',
        65,
        'SHOW'
    );


-- =========================================================
-- 7. FMCG - DEALER VISIT
-- dealerType = Other -> show otherDealerType
-- =========================================================

INSERT INTO field_rules
(
    form_id,
    visit_type_id,
    source_field_id,
    operator,
    expected_value,
    target_field_id,
    action
)
VALUES
    (
        11,
        11,
        96,
        'EQUALS',
        'Other',
        97,
        'SHOW'
    );


-- =========================================================
-- 8. FMCG - CORPORATE VISIT
-- businessType = Other -> show otherBusinessType
-- =========================================================

INSERT INTO field_rules
(
    form_id,
    visit_type_id,
    source_field_id,
    operator,
    expected_value,
    target_field_id,
    action
)
VALUES
    (
        12,
        12,
        105,
        'EQUALS',
        'Other',
        106,
        'SHOW'
    );


-- =========================================================
-- 9. FMCG - CORPORATE PERSON VISIT
-- department = Other -> show otherDepartment
-- =========================================================

INSERT INTO field_rules
(
    form_id,
    visit_type_id,
    source_field_id,
    operator,
    expected_value,
    target_field_id,
    action
)
VALUES
    (
        13,
        13,
        111,
        'EQUALS',
        'Other',
        112,
        'SHOW'
    );


-- =========================================================
-- 10. FMCG - CORPORATE PERSON VISIT
-- decisionArea = Other -> show otherDecisionArea
-- =========================================================

INSERT INTO field_rules
(
    form_id,
    visit_type_id,
    source_field_id,
    operator,
    expected_value,
    target_field_id,
    action
)
VALUES
    (
        13,
        13,
        116,
        'EQUALS',
        'Other',
        117,
        'SHOW'
    );

-- =========================================================
-- END V8
-- =========================================================