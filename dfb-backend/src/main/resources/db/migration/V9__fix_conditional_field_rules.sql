-- ============================================================
-- V9: Fix conditional field rules
-- ============================================================

-- ------------------------------------------------------------
-- 1. Decision Area is conditionally applicable.
--    It is required only when Decision Maker = Yes.
-- ------------------------------------------------------------

UPDATE form_fields
SET required = FALSE
WHERE id IN (64, 116);

-- ------------------------------------------------------------
-- 2. Poultry Dealer:
--    dealerType = Other -> show otherDealerType
-- ------------------------------------------------------------

INSERT INTO field_rules (
    form_id,
    visit_type_id,
    source_field_id,
    operator,
    expected_value,
    target_field_id,
    action
)
VALUES (
           9,
           9,
           77,
           'EQUALS',
           'Other',
           78,
           'SHOW'
       );

-- ------------------------------------------------------------
-- 3. Poultry Corporate Office Person:
--    decisionMaker = Yes -> show decisionArea
-- ------------------------------------------------------------

INSERT INTO field_rules (
    form_id,
    visit_type_id,
    source_field_id,
    operator,
    expected_value,
    target_field_id,
    action
)
VALUES (
           8,
           8,
           63,
           'EQUALS',
           'Yes',
           64,
           'SHOW'
       );

-- ------------------------------------------------------------
-- 4. FMCG Corporate Person:
--    decisionMaker = Yes -> show decisionArea
-- ------------------------------------------------------------

INSERT INTO field_rules (
    form_id,
    visit_type_id,
    source_field_id,
    operator,
    expected_value,
    target_field_id,
    action
)
VALUES (
           13,
           13,
           115,
           'EQUALS',
           'Yes',
           116,
           'SHOW'
       );