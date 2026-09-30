-- =========================================================
-- V7.2 - SEED FORM FIELD TO VISIT TYPE MAPPINGS
-- =========================================================
--
-- Each visit type receives all active fields belonging
-- to its corresponding form.
--
-- =========================================================


-- =========================================================
-- 1. AGRO - DEALER VISIT
-- =========================================================

INSERT INTO form_field_visit_types
(
    form_field_id,
    visit_type_id
)
SELECT
    ff.id,
    vt.id
FROM form_fields ff
         JOIN forms f
              ON f.id = ff.form_id
         JOIN visit_types vt
              ON vt.name = 'Dealer Visit'
         JOIN domains d
              ON d.id = vt.domain_id
WHERE f.code = 'AGRO_DEALER_VISIT'
  AND d.code = 'AGRO'
  AND ff.active = TRUE;


-- =========================================================
-- 2. AGRO - FARMER VISIT
-- =========================================================

INSERT INTO form_field_visit_types
(
    form_field_id,
    visit_type_id
)
SELECT
    ff.id,
    vt.id
FROM form_fields ff
         JOIN forms f
              ON f.id = ff.form_id
         JOIN visit_types vt
              ON vt.name = 'Farmer Visit'
         JOIN domains d
              ON d.id = vt.domain_id
WHERE f.code = 'AGRO_FARMER_VISIT'
  AND d.code = 'AGRO'
  AND ff.active = TRUE;


-- =========================================================
-- 3. AGRO - NURSERY VISIT
-- =========================================================

INSERT INTO form_field_visit_types
(
    form_field_id,
    visit_type_id
)
SELECT
    ff.id,
    vt.id
FROM form_fields ff
         JOIN forms f
              ON f.id = ff.form_id
         JOIN visit_types vt
              ON vt.name = 'Nursery Visit'
         JOIN domains d
              ON d.id = vt.domain_id
WHERE f.code = 'AGRO_NURSERY_VISIT'
  AND d.code = 'AGRO'
  AND ff.active = TRUE;


-- =========================================================
-- 4. POULTRY - BRANCH
-- =========================================================

INSERT INTO form_field_visit_types
(
    form_field_id,
    visit_type_id
)
SELECT
    ff.id,
    vt.id
FROM form_fields ff
         JOIN forms f
              ON f.id = ff.form_id
         JOIN visit_types vt
              ON vt.name = 'Branch'
         JOIN domains d
              ON d.id = vt.domain_id
WHERE f.code = 'POULTRY_BRANCH_VISIT'
  AND d.code = 'POULTRY'
  AND ff.active = TRUE;


-- =========================================================
-- 5. POULTRY - BRANCH PERSON
-- =========================================================

INSERT INTO form_field_visit_types
(
    form_field_id,
    visit_type_id
)
SELECT
    ff.id,
    vt.id
FROM form_fields ff
         JOIN forms f
              ON f.id = ff.form_id
         JOIN visit_types vt
              ON vt.name = 'Branch Person'
         JOIN domains d
              ON d.id = vt.domain_id
WHERE f.code = 'POULTRY_BRANCH_PERSON_VISIT'
  AND d.code = 'POULTRY'
  AND ff.active = TRUE;


-- =========================================================
-- 6. POULTRY - DOCTOR
-- =========================================================

INSERT INTO form_field_visit_types
(
    form_field_id,
    visit_type_id
)
SELECT
    ff.id,
    vt.id
FROM form_fields ff
         JOIN forms f
              ON f.id = ff.form_id
         JOIN visit_types vt
              ON vt.name = 'Doctor'
         JOIN domains d
              ON d.id = vt.domain_id
WHERE f.code = 'POULTRY_DOCTOR_VISIT'
  AND d.code = 'POULTRY'
  AND ff.active = TRUE;


-- =========================================================
-- 7. POULTRY - CORPORATE OFFICE
-- =========================================================

INSERT INTO form_field_visit_types
(
    form_field_id,
    visit_type_id
)
SELECT
    ff.id,
    vt.id
FROM form_fields ff
         JOIN forms f
              ON f.id = ff.form_id
         JOIN visit_types vt
              ON vt.name = 'Corporate Office'
         JOIN domains d
              ON d.id = vt.domain_id
WHERE f.code = 'POULTRY_CORPORATE_OFFICE_VISIT'
  AND d.code = 'POULTRY'
  AND ff.active = TRUE;


-- =========================================================
-- 8. POULTRY - CORPORATE OFFICE PERSON
-- =========================================================

INSERT INTO form_field_visit_types
(
    form_field_id,
    visit_type_id
)
SELECT
    ff.id,
    vt.id
FROM form_fields ff
         JOIN forms f
              ON f.id = ff.form_id
         JOIN visit_types vt
              ON vt.name = 'Corporate Office Person'
         JOIN domains d
              ON d.id = vt.domain_id
WHERE f.code = 'POULTRY_CORPORATE_OFFICE_PERSON_VISIT'
  AND d.code = 'POULTRY'
  AND ff.active = TRUE;


-- =========================================================
-- 9. POULTRY - DEALER
-- =========================================================

INSERT INTO form_field_visit_types
(
    form_field_id,
    visit_type_id
)
SELECT
    ff.id,
    vt.id
FROM form_fields ff
         JOIN forms f
              ON f.id = ff.form_id
         JOIN visit_types vt
              ON vt.name = 'Dealer'
         JOIN domains d
              ON d.id = vt.domain_id
WHERE f.code = 'POULTRY_DEALER_VISIT'
  AND d.code = 'POULTRY'
  AND ff.active = TRUE;


-- =========================================================
-- 10. POULTRY - FARMER
-- =========================================================

INSERT INTO form_field_visit_types
(
    form_field_id,
    visit_type_id
)
SELECT
    ff.id,
    vt.id
FROM form_fields ff
         JOIN forms f
              ON f.id = ff.form_id
         JOIN visit_types vt
              ON vt.name = 'Poultry Farmer'
         JOIN domains d
              ON d.id = vt.domain_id
WHERE f.code = 'POULTRY_FARMER_VISIT'
  AND d.code = 'POULTRY'
  AND ff.active = TRUE;


-- =========================================================
-- 11. FMCG - DEALER
-- =========================================================

INSERT INTO form_field_visit_types
(
    form_field_id,
    visit_type_id
)
SELECT
    ff.id,
    vt.id
FROM form_fields ff
         JOIN forms f
              ON f.id = ff.form_id
         JOIN visit_types vt
              ON vt.name = 'Dealer'
         JOIN domains d
              ON d.id = vt.domain_id
WHERE f.code = 'FMCG_DEALER_VISIT'
  AND d.code = 'FMCG'
  AND ff.active = TRUE;


-- =========================================================
-- 12. FMCG - CORPORATE
-- =========================================================

INSERT INTO form_field_visit_types
(
    form_field_id,
    visit_type_id
)
SELECT
    ff.id,
    vt.id
FROM form_fields ff
         JOIN forms f
              ON f.id = ff.form_id
         JOIN visit_types vt
              ON vt.name = 'Corporate'
         JOIN domains d
              ON d.id = vt.domain_id
WHERE f.code = 'FMCG_CORPORATE_VISIT'
  AND d.code = 'FMCG'
  AND ff.active = TRUE;


-- =========================================================
-- 13. FMCG - CORPORATE PERSON
-- =========================================================

INSERT INTO form_field_visit_types
(
    form_field_id,
    visit_type_id
)
SELECT
    ff.id,
    vt.id
FROM form_fields ff
         JOIN forms f
              ON f.id = ff.form_id
         JOIN visit_types vt
              ON vt.name = 'Corporate Person'
         JOIN domains d
              ON d.id = vt.domain_id
WHERE f.code = 'FMCG_CORPORATE_PERSON_VISIT'
  AND d.code = 'FMCG'
  AND ff.active = TRUE;


-- =========================================================
-- END V7.2
-- =========================================================