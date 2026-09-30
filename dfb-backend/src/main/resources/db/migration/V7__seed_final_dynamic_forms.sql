-- =========================================================
-- V7 - SEED FINAL DYNAMIC FORMS / FIELDS / OPTIONS
-- =========================================================
--
-- SOURCE OF TRUTH:
-- DFB Final 13 Visit-Type Field Master PDF
--
-- This migration seeds:
--   1. 13 Forms
--   2. All finalized Form Fields
--   3. All SELECT / MULTI_SELECT Options
--
-- Visit-Type mappings are handled separately.
-- Field Rules are handled separately.
--
-- Added By:
--   SYSTEM field
--   Mandatory
--   Populated from authenticated JWT user at submission time
--
-- =========================================================


-- =========================================================
-- 1. FORMS
-- =========================================================

INSERT INTO forms
(
    domain_id,
    name,
    code,
    description,
    active
)
SELECT
    d.id,
    'Dealer Visit',
    'AGRO_DEALER_VISIT',
    'Agro Dealer Visit',
    TRUE
FROM domains d
WHERE d.code = 'AGRO';

INSERT INTO forms
(
    domain_id,
    name,
    code,
    description,
    active
)
SELECT
    d.id,
    'Farmer Visit',
    'AGRO_FARMER_VISIT',
    'Agro Farmer Visit',
    TRUE
FROM domains d
WHERE d.code = 'AGRO';

INSERT INTO forms
(
    domain_id,
    name,
    code,
    description,
    active
)
SELECT
    d.id,
    'Nursery Visit',
    'AGRO_NURSERY_VISIT',
    'Agro Nursery Visit',
    TRUE
FROM domains d
WHERE d.code = 'AGRO';


-- =========================================================
-- POULTRY
-- =========================================================

INSERT INTO forms
(
    domain_id,
    name,
    code,
    description,
    active
)
SELECT
    d.id,
    'Branch Visit',
    'POULTRY_BRANCH_VISIT',
    'Poultry Branch Visit',
    TRUE
FROM domains d
WHERE d.code = 'POULTRY';

INSERT INTO forms
(
    domain_id,
    name,
    code,
    description,
    active
)
SELECT
    d.id,
    'Branch Person Visit',
    'POULTRY_BRANCH_PERSON_VISIT',
    'Poultry Branch Person Visit',
    TRUE
FROM domains d
WHERE d.code = 'POULTRY';

INSERT INTO forms
(
    domain_id,
    name,
    code,
    description,
    active
)
SELECT
    d.id,
    'Doctor Visit',
    'POULTRY_DOCTOR_VISIT',
    'Poultry Doctor Visit',
    TRUE
FROM domains d
WHERE d.code = 'POULTRY';

INSERT INTO forms
(
    domain_id,
    name,
    code,
    description,
    active
)
SELECT
    d.id,
    'Corporate Office Visit',
    'POULTRY_CORPORATE_OFFICE_VISIT',
    'Poultry Corporate Office Visit',
    TRUE
FROM domains d
WHERE d.code = 'POULTRY';

INSERT INTO forms
(
    domain_id,
    name,
    code,
    description,
    active
)
SELECT
    d.id,
    'Corporate Office Person Visit',
    'POULTRY_CORPORATE_OFFICE_PERSON_VISIT',
    'Poultry Corporate Office Person Visit',
    TRUE
FROM domains d
WHERE d.code = 'POULTRY';

INSERT INTO forms
(
    domain_id,
    name,
    code,
    description,
    active
)
SELECT
    d.id,
    'Dealer Visit',
    'POULTRY_DEALER_VISIT',
    'Poultry Dealer Visit',
    TRUE
FROM domains d
WHERE d.code = 'POULTRY';

INSERT INTO forms
(
    domain_id,
    name,
    code,
    description,
    active
)
SELECT
    d.id,
    'Poultry Farmer Visit',
    'POULTRY_FARMER_VISIT',
    'Poultry Farmer Visit',
    TRUE
FROM domains d
WHERE d.code = 'POULTRY';


-- =========================================================
-- FMCG
-- =========================================================

INSERT INTO forms
(
    domain_id,
    name,
    code,
    description,
    active
)
SELECT
    d.id,
    'Dealer Visit',
    'FMCG_DEALER_VISIT',
    'FMCG Dealer Visit',
    TRUE
FROM domains d
WHERE d.code = 'FMCG';

INSERT INTO forms
(
    domain_id,
    name,
    code,
    description,
    active
)
SELECT
    d.id,
    'Corporate Visit',
    'FMCG_CORPORATE_VISIT',
    'FMCG Corporate Visit',
    TRUE
FROM domains d
WHERE d.code = 'FMCG';

INSERT INTO forms
(
    domain_id,
    name,
    code,
    description,
    active
)
SELECT
    d.id,
    'Corporate Person Visit',
    'FMCG_CORPORATE_PERSON_VISIT',
    'FMCG Corporate Person Visit',
    TRUE
FROM domains d
WHERE d.code = 'FMCG';


-- =========================================================
-- 2. FORM FIELDS
-- =========================================================


-- =========================================================
-- 1. AGRO - DEALER VISIT
-- =========================================================

INSERT INTO form_fields
(
    form_id,
    field_key,
    label,
    field_type,
    required,
    display_order,
    validation_rule,
    active
)
SELECT f.id, 'shopName', 'Shop Name', 'TEXT', TRUE, 1, NULL, TRUE
FROM forms f WHERE f.code = 'AGRO_DEALER_VISIT'
UNION ALL
SELECT f.id, 'ownerName', 'Owner Name', 'TEXT', TRUE, 2, NULL, TRUE
FROM forms f WHERE f.code = 'AGRO_DEALER_VISIT'
UNION ALL
SELECT f.id, 'phone', 'Phone', 'NUMBER', TRUE, 3, 'Exactly 10 digits', TRUE
FROM forms f WHERE f.code = 'AGRO_DEALER_VISIT'
UNION ALL
SELECT f.id, 'address', 'Address', 'TEXTAREA', TRUE, 4, NULL, TRUE
FROM forms f WHERE f.code = 'AGRO_DEALER_VISIT'
UNION ALL
SELECT f.id, 'dealerType', 'Dealer Type', 'SELECT', TRUE, 5, NULL, TRUE
FROM forms f WHERE f.code = 'AGRO_DEALER_VISIT'
UNION ALL
SELECT f.id, 'otherDealerType', 'Other Dealer Type', 'TEXT', FALSE, 6,
       'Show when dealerType = Other', TRUE
FROM forms f WHERE f.code = 'AGRO_DEALER_VISIT'
UNION ALL
SELECT f.id, 'addedBy', 'Added By', 'SYSTEM', TRUE, 7,
       'Logged-in user via JWT', TRUE
FROM forms f WHERE f.code = 'AGRO_DEALER_VISIT';


-- =========================================================
-- 2. AGRO - FARMER VISIT
-- =========================================================

INSERT INTO form_fields
(
    form_id,
    field_key,
    label,
    field_type,
    required,
    display_order,
    validation_rule,
    active
)
SELECT f.id, 'farmerName', 'Farmer Name', 'TEXT', TRUE, 1, NULL, TRUE
FROM forms f WHERE f.code = 'AGRO_FARMER_VISIT'
UNION ALL
SELECT f.id, 'phone', 'Phone', 'NUMBER', TRUE, 2,
       'Exactly 10 digits', TRUE
FROM forms f WHERE f.code = 'AGRO_FARMER_VISIT'
UNION ALL
SELECT f.id, 'address', 'Address', 'TEXTAREA', TRUE, 3, NULL, TRUE
FROM forms f WHERE f.code = 'AGRO_FARMER_VISIT'
UNION ALL
SELECT f.id, 'crop', 'Crop', 'MULTI_SELECT', TRUE, 4,
       'Maharashtra crop list + Other', TRUE
FROM forms f WHERE f.code = 'AGRO_FARMER_VISIT'
UNION ALL
SELECT f.id, 'otherCrop', 'Other Crop', 'TEXT', FALSE, 5,
       'Show when Other selected', TRUE
FROM forms f WHERE f.code = 'AGRO_FARMER_VISIT'
UNION ALL
SELECT f.id, 'addedBy', 'Added By', 'SYSTEM', TRUE, 6,
       'Logged-in user via JWT', TRUE
FROM forms f WHERE f.code = 'AGRO_FARMER_VISIT';


-- =========================================================
-- 3. AGRO - NURSERY VISIT
-- =========================================================

INSERT INTO form_fields
(
    form_id,
    field_key,
    label,
    field_type,
    required,
    display_order,
    validation_rule,
    active
)
SELECT f.id, 'nurseryName', 'Nursery Name', 'TEXT', TRUE, 1, NULL, TRUE
FROM forms f WHERE f.code = 'AGRO_NURSERY_VISIT'
UNION ALL
SELECT f.id, 'ownerName', 'Owner Name', 'TEXT', TRUE, 2, NULL, TRUE
FROM forms f WHERE f.code = 'AGRO_NURSERY_VISIT'
UNION ALL
SELECT f.id, 'phone', 'Phone', 'NUMBER', TRUE, 3,
       'Exactly 10 digits', TRUE
FROM forms f WHERE f.code = 'AGRO_NURSERY_VISIT'
UNION ALL
SELECT f.id, 'address', 'Address', 'TEXTAREA', TRUE, 4, NULL, TRUE
FROM forms f WHERE f.code = 'AGRO_NURSERY_VISIT'
UNION ALL
SELECT f.id, 'nurseryType', 'Nursery Type', 'SELECT', TRUE, 5, NULL, TRUE
FROM forms f WHERE f.code = 'AGRO_NURSERY_VISIT'
UNION ALL
SELECT f.id, 'otherNurseryType', 'Other Nursery Type', 'TEXT', FALSE, 6,
       'Show when Other selected', TRUE
FROM forms f WHERE f.code = 'AGRO_NURSERY_VISIT'
UNION ALL
SELECT f.id, 'productionCapacity', 'Production Capacity', 'NUMBER', TRUE, 7,
       'No min/max', TRUE
FROM forms f WHERE f.code = 'AGRO_NURSERY_VISIT'
UNION ALL
SELECT f.id, 'addedBy', 'Added By', 'SYSTEM', TRUE, 8,
       'Logged-in user via JWT', TRUE
FROM forms f WHERE f.code = 'AGRO_NURSERY_VISIT';


-- =========================================================
-- 4. POULTRY - BRANCH VISIT
-- =========================================================

INSERT INTO form_fields
(
    form_id,
    field_key,
    label,
    field_type,
    required,
    display_order,
    validation_rule,
    active
)
SELECT f.id, 'companyName', 'Company Name', 'TEXT', TRUE, 1, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_BRANCH_VISIT'
UNION ALL
SELECT f.id, 'branchName', 'Branch Name', 'TEXT', TRUE, 2, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_BRANCH_VISIT'
UNION ALL
SELECT f.id, 'branchAddress', 'Branch Address', 'TEXTAREA', TRUE, 3, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_BRANCH_VISIT'
UNION ALL
SELECT f.id, 'birdSegment', 'Bird Segment', 'SELECT', TRUE, 4, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_BRANCH_VISIT'
UNION ALL
SELECT f.id, 'annualBirdPlacement', 'Annual Bird Placement', 'NUMBER', TRUE, 5,
       'No min/max', TRUE
FROM forms f WHERE f.code = 'POULTRY_BRANCH_VISIT'
UNION ALL
SELECT f.id, 'numberOfFarmers', 'Number of Farmers', 'NUMBER', TRUE, 6,
       'Numeric', TRUE
FROM forms f WHERE f.code = 'POULTRY_BRANCH_VISIT'
UNION ALL
SELECT f.id, 'addedBy', 'Added By', 'SYSTEM', TRUE, 7,
       'Logged-in user via JWT', TRUE
FROM forms f WHERE f.code = 'POULTRY_BRANCH_VISIT';


-- =========================================================
-- 5. POULTRY - BRANCH PERSON VISIT
-- =========================================================

INSERT INTO form_fields
(
    form_id,
    field_key,
    label,
    field_type,
    required,
    display_order,
    validation_rule,
    active
)
SELECT f.id, 'name', 'Name', 'TEXT', TRUE, 1, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_BRANCH_PERSON_VISIT'
UNION ALL
SELECT f.id, 'designation', 'Designation', 'SELECT', TRUE, 2, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_BRANCH_PERSON_VISIT'
UNION ALL
SELECT f.id, 'otherDesignation', 'Other Designation', 'TEXT', FALSE, 3,
       'Show when Other selected', TRUE
FROM forms f WHERE f.code = 'POULTRY_BRANCH_PERSON_VISIT'
UNION ALL
SELECT f.id, 'phone', 'Phone', 'NUMBER', TRUE, 4,
       'Exactly 10 digits', TRUE
FROM forms f WHERE f.code = 'POULTRY_BRANCH_PERSON_VISIT'
UNION ALL
SELECT f.id, 'visitArea', 'Area', 'TEXT', TRUE, 5, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_BRANCH_PERSON_VISIT'
UNION ALL
SELECT f.id, 'numberOfFarmers', 'Number of Farmers', 'NUMBER', TRUE, 6,
       'Numeric', TRUE
FROM forms f WHERE f.code = 'POULTRY_BRANCH_PERSON_VISIT'
UNION ALL
SELECT f.id, 'addedBy', 'Added By', 'SYSTEM', TRUE, 7,
       'Logged-in user via JWT', TRUE
FROM forms f WHERE f.code = 'POULTRY_BRANCH_PERSON_VISIT';


-- =========================================================
-- 6. POULTRY - DOCTOR VISIT
-- =========================================================

INSERT INTO form_fields
(
    form_id,
    field_key,
    label,
    field_type,
    required,
    display_order,
    validation_rule,
    active
)
SELECT f.id, 'doctorName', 'Doctor Name', 'TEXT', TRUE, 1, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_DOCTOR_VISIT'
UNION ALL
SELECT f.id, 'phone', 'Phone', 'NUMBER', TRUE, 2,
       'Exactly 10 digits', TRUE
FROM forms f WHERE f.code = 'POULTRY_DOCTOR_VISIT'
UNION ALL
SELECT f.id, 'doctorType', 'Type', 'SELECT', TRUE, 3, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_DOCTOR_VISIT'
UNION ALL
SELECT f.id, 'companyName', 'Company Name', 'TEXT', TRUE, 4, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_DOCTOR_VISIT'
UNION ALL
SELECT f.id, 'visitArea', 'Visit Area', 'TEXT', TRUE, 5, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_DOCTOR_VISIT'
UNION ALL
SELECT f.id, 'numberOfFarmers', 'Number of Farmers', 'NUMBER', TRUE, 6,
       'Numeric', TRUE
FROM forms f WHERE f.code = 'POULTRY_DOCTOR_VISIT'
UNION ALL
SELECT f.id, 'addedBy', 'Added By', 'SYSTEM', TRUE, 7,
       'Logged-in user via JWT', TRUE
FROM forms f WHERE f.code = 'POULTRY_DOCTOR_VISIT';


-- =========================================================
-- 7. POULTRY - CORPORATE OFFICE VISIT
-- =========================================================

INSERT INTO form_fields
(
    form_id,
    field_key,
    label,
    field_type,
    required,
    display_order,
    validation_rule,
    active
)
SELECT f.id, 'companyName', 'Company Name', 'TEXT', TRUE, 1, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_CORPORATE_OFFICE_VISIT'
UNION ALL
SELECT f.id, 'headquartersName', 'Headquarters Name', 'TEXT', TRUE, 2, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_CORPORATE_OFFICE_VISIT'
UNION ALL
SELECT f.id, 'address', 'Address', 'TEXTAREA', TRUE, 3, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_CORPORATE_OFFICE_VISIT'
UNION ALL
SELECT f.id, 'businessType', 'Business Type', 'SELECT', TRUE, 4, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_CORPORATE_OFFICE_VISIT'
UNION ALL
SELECT f.id, 'otherBusinessType', 'Other Business Type', 'TEXT', FALSE, 5,
       'Show when Other selected', TRUE
FROM forms f WHERE f.code = 'POULTRY_CORPORATE_OFFICE_VISIT'
UNION ALL
SELECT f.id, 'addedBy', 'Added By', 'SYSTEM', TRUE, 6,
       'Logged-in user via JWT', TRUE
FROM forms f WHERE f.code = 'POULTRY_CORPORATE_OFFICE_VISIT';


-- =========================================================
-- 8. POULTRY - CORPORATE OFFICE PERSON VISIT
-- =========================================================

INSERT INTO form_fields
(
    form_id,
    field_key,
    label,
    field_type,
    required,
    display_order,
    validation_rule,
    active
)
SELECT f.id, 'personName', 'Person Name', 'TEXT', TRUE, 1, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_CORPORATE_OFFICE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'designation', 'Designation', 'TEXT', TRUE, 2, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_CORPORATE_OFFICE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'department', 'Department', 'TEXT', TRUE, 3, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_CORPORATE_OFFICE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'phone', 'Phone', 'NUMBER', TRUE, 4,
       'Exactly 10 digits', TRUE
FROM forms f WHERE f.code = 'POULTRY_CORPORATE_OFFICE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'email', 'Email', 'EMAIL', TRUE, 5,
       'Valid email', TRUE
FROM forms f WHERE f.code = 'POULTRY_CORPORATE_OFFICE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'decisionMaker', 'Decision Maker', 'SELECT', TRUE, 6, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_CORPORATE_OFFICE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'decisionArea', 'Decision Area', 'SELECT', TRUE, 7,
       'Single selection', TRUE
FROM forms f WHERE f.code = 'POULTRY_CORPORATE_OFFICE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'otherDecisionArea', 'Other Decision Area', 'TEXT', FALSE, 8,
       'Show when Other selected', TRUE
FROM forms f WHERE f.code = 'POULTRY_CORPORATE_OFFICE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'addedBy', 'Added By', 'SYSTEM', TRUE, 9,
       'Logged-in user via JWT', TRUE
FROM forms f WHERE f.code = 'POULTRY_CORPORATE_OFFICE_PERSON_VISIT';


-- =========================================================
-- 9. POULTRY - DEALER VISIT
-- =========================================================

INSERT INTO form_fields
(
    form_id,
    field_key,
    label,
    field_type,
    required,
    display_order,
    validation_rule,
    active
)
SELECT f.id, 'shopName', 'Shop Name', 'TEXT', TRUE, 1, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_DEALER_VISIT'
UNION ALL
SELECT f.id, 'ownerName', 'Owner Name', 'TEXT', TRUE, 2, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_DEALER_VISIT'
UNION ALL
SELECT f.id, 'phone', 'Phone', 'NUMBER', TRUE, 3,
       'Exactly 10 digits', TRUE
FROM forms f WHERE f.code = 'POULTRY_DEALER_VISIT'
UNION ALL
SELECT f.id, 'address', 'Address', 'TEXTAREA', TRUE, 4, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_DEALER_VISIT'
UNION ALL
SELECT f.id, 'dealerType', 'Dealer Type', 'SELECT', TRUE, 5, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_DEALER_VISIT'
UNION ALL
SELECT f.id, 'otherDealerType', 'Other Dealer Type', 'TEXT', FALSE, 6,
       'Show when Other selected', TRUE
FROM forms f WHERE f.code = 'POULTRY_DEALER_VISIT'
UNION ALL
SELECT f.id, 'addedBy', 'Added By', 'SYSTEM', TRUE, 7,
       'Logged-in user via JWT', TRUE
FROM forms f WHERE f.code = 'POULTRY_DEALER_VISIT';


-- =========================================================
-- 10. POULTRY - FARMER VISIT
-- =========================================================

INSERT INTO form_fields
(
    form_id,
    field_key,
    label,
    field_type,
    required,
    display_order,
    validation_rule,
    active
)
SELECT f.id, 'farmerName', 'Farmer Name', 'TEXT', TRUE, 1, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_FARMER_VISIT'
UNION ALL
SELECT f.id, 'phone', 'Phone', 'NUMBER', TRUE, 2,
       'Exactly 10 digits', TRUE
FROM forms f WHERE f.code = 'POULTRY_FARMER_VISIT'
UNION ALL
SELECT f.id, 'address', 'Address', 'TEXTAREA', TRUE, 3, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_FARMER_VISIT'
UNION ALL
SELECT f.id, 'type', 'Type', 'SELECT', TRUE, 4, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_FARMER_VISIT'
UNION ALL
SELECT f.id, 'company', 'Company', 'TEXT', TRUE, 5, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_FARMER_VISIT'
UNION ALL
SELECT f.id, 'capacity', 'Capacity (Number of Birds)', 'NUMBER', TRUE, 6,
       'Numeric', TRUE
FROM forms f WHERE f.code = 'POULTRY_FARMER_VISIT'
UNION ALL
SELECT f.id, 'purchasesMedicineFrom', 'Purchases Medicine From', 'TEXT',
       TRUE, 7, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_FARMER_VISIT'
UNION ALL
SELECT f.id, 'consultationMedicineFrom',
       'Consultation / Medicine From', 'TEXT', TRUE, 8, NULL, TRUE
FROM forms f WHERE f.code = 'POULTRY_FARMER_VISIT'
UNION ALL
SELECT f.id, 'addedBy', 'Added By', 'SYSTEM', TRUE, 9,
       'Logged-in user via JWT', TRUE
FROM forms f WHERE f.code = 'POULTRY_FARMER_VISIT';


-- =========================================================
-- 11. FMCG - DEALER VISIT
-- =========================================================

INSERT INTO form_fields
(
    form_id,
    field_key,
    label,
    field_type,
    required,
    display_order,
    validation_rule,
    active
)
SELECT f.id, 'shopName', 'Shop Name', 'TEXT', TRUE, 1, NULL, TRUE
FROM forms f WHERE f.code = 'FMCG_DEALER_VISIT'
UNION ALL
SELECT f.id, 'dealerType', 'Type', 'SELECT', TRUE, 2, NULL, TRUE
FROM forms f WHERE f.code = 'FMCG_DEALER_VISIT'
UNION ALL
SELECT f.id, 'otherDealerType', 'Other Dealer Type', 'TEXT', FALSE, 3,
       'Show when Other selected', TRUE
FROM forms f WHERE f.code = 'FMCG_DEALER_VISIT'
UNION ALL
SELECT f.id, 'phone', 'Phone', 'NUMBER', TRUE, 4,
       'Exactly 10 digits', TRUE
FROM forms f WHERE f.code = 'FMCG_DEALER_VISIT'
UNION ALL
SELECT f.id, 'address', 'Address', 'TEXTAREA', TRUE, 5, NULL, TRUE
FROM forms f WHERE f.code = 'FMCG_DEALER_VISIT'
UNION ALL
SELECT f.id, 'addedBy', 'Added By', 'SYSTEM', TRUE, 6,
       'Logged-in user via JWT', TRUE
FROM forms f WHERE f.code = 'FMCG_DEALER_VISIT';


-- =========================================================
-- 12. FMCG - CORPORATE VISIT
-- =========================================================

INSERT INTO form_fields
(
    form_id,
    field_key,
    label,
    field_type,
    required,
    display_order,
    validation_rule,
    active
)
SELECT f.id, 'companyName', 'Company Name', 'TEXT', TRUE, 1, NULL, TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_VISIT'
UNION ALL
SELECT f.id, 'address', 'Address', 'TEXTAREA', TRUE, 2, NULL, TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_VISIT'
UNION ALL
SELECT f.id, 'headquartersName', 'Headquarters Name', 'TEXT', TRUE, 3,
       NULL, TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_VISIT'
UNION ALL
SELECT f.id, 'businessType', 'Business Type', 'SELECT', TRUE, 4,
       NULL, TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_VISIT'
UNION ALL
SELECT f.id, 'otherBusinessType', 'Other Business Type', 'TEXT', FALSE, 5,
       'Show when Other selected', TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_VISIT'
UNION ALL
SELECT f.id, 'addedBy', 'Added By', 'SYSTEM', TRUE, 6,
       'Logged-in user via JWT', TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_VISIT';


-- =========================================================
-- 13. FMCG - CORPORATE PERSON VISIT
-- =========================================================

INSERT INTO form_fields
(
    form_id,
    field_key,
    label,
    field_type,
    required,
    display_order,
    validation_rule,
    active
)
SELECT f.id, 'name', 'Name', 'TEXT', TRUE, 1, NULL, TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'designation', 'Designation', 'TEXT', TRUE, 2, NULL, TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'department', 'Department', 'SELECT', TRUE, 3, NULL, TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'otherDepartment', 'Other Department', 'TEXT', FALSE, 4,
       'Show when Other selected', TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'phone', 'Phone', 'NUMBER', TRUE, 5,
       'Exactly 10 digits', TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'email', 'Email', 'EMAIL', TRUE, 6,
       'Valid email', TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'decisionMaker', 'Decision Maker', 'SELECT', TRUE, 7, NULL, TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'decisionArea', 'Decision Area', 'SELECT', TRUE, 8,
       'Single selection', TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'otherDecisionArea', 'Other Decision Area', 'TEXT', FALSE, 9,
       'Show when Other selected', TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'remarks', 'Remarks', 'TEXTAREA', TRUE, 10, NULL, TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_PERSON_VISIT'
UNION ALL
SELECT f.id, 'addedBy', 'Added By', 'SYSTEM', TRUE, 11,
       'Logged-in user via JWT', TRUE
FROM forms f WHERE f.code = 'FMCG_CORPORATE_PERSON_VISIT';


-- =========================================================
-- 3. FIELD OPTIONS
-- =========================================================


-- =========================================================
-- AGRO DEALER - dealerType
-- =========================================================

INSERT INTO field_options
(
    field_id,
    label,
    value,
    display_order,
    active
)
SELECT ff.id, 'Retailer', 'Retailer', 1, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
WHERE f.code = 'AGRO_DEALER_VISIT'
  AND ff.field_key = 'dealerType'
UNION ALL
SELECT ff.id, 'Distributor', 'Distributor', 2, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
WHERE f.code = 'AGRO_DEALER_VISIT'
  AND ff.field_key = 'dealerType'
UNION ALL
SELECT ff.id, 'Wholesaler', 'Wholesaler', 3, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
WHERE f.code = 'AGRO_DEALER_VISIT'
  AND ff.field_key = 'dealerType'
UNION ALL
SELECT ff.id, 'Other', 'Other', 4, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
WHERE f.code = 'AGRO_DEALER_VISIT'
  AND ff.field_key = 'dealerType';


-- =========================================================
-- AGRO FARMER - crop
-- =========================================================

INSERT INTO field_options
(
    field_id,
    label,
    value,
    display_order,
    active
)
SELECT ff.id, x.label, x.label, x.display_order, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
         JOIN
     (
         SELECT 1 display_order, 'Rice' label
         UNION ALL SELECT 2, 'Wheat'
         UNION ALL SELECT 3, 'Maize'
         UNION ALL SELECT 4, 'Jowar'
         UNION ALL SELECT 5, 'Bajra'
         UNION ALL SELECT 6, 'Ragi'
         UNION ALL SELECT 7, 'Tur'
         UNION ALL SELECT 8, 'Urad'
         UNION ALL SELECT 9, 'Moong'
         UNION ALL SELECT 10, 'Chickpea / Gram'
         UNION ALL SELECT 11, 'Lentil'
         UNION ALL SELECT 12, 'Peas'
         UNION ALL SELECT 13, 'Soybean'
         UNION ALL SELECT 14, 'Groundnut'
         UNION ALL SELECT 15, 'Sunflower'
         UNION ALL SELECT 16, 'Safflower'
         UNION ALL SELECT 17, 'Sesame'
         UNION ALL SELECT 18, 'Castor'
         UNION ALL SELECT 19, 'Cotton'
         UNION ALL SELECT 20, 'Sugarcane'
         UNION ALL SELECT 21, 'Onion'
         UNION ALL SELECT 22, 'Potato'
         UNION ALL SELECT 23, 'Tomato'
         UNION ALL SELECT 24, 'Brinjal'
         UNION ALL SELECT 25, 'Cabbage'
         UNION ALL SELECT 26, 'Cauliflower'
         UNION ALL SELECT 27, 'Chilli'
         UNION ALL SELECT 28, 'Capsicum'
         UNION ALL SELECT 29, 'Okra / Bhindi'
         UNION ALL SELECT 30, 'Cucumber'
         UNION ALL SELECT 31, 'Gourds'
         UNION ALL SELECT 32, 'Garlic'
         UNION ALL SELECT 33, 'Turmeric'
         UNION ALL SELECT 34, 'Ginger'
         UNION ALL SELECT 35, 'Mango'
         UNION ALL SELECT 36, 'Grapes'
         UNION ALL SELECT 37, 'Banana'
         UNION ALL SELECT 38, 'Pomegranate'
         UNION ALL SELECT 39, 'Papaya'
         UNION ALL SELECT 40, 'Watermelon'
         UNION ALL SELECT 41, 'Muskmelon'
         UNION ALL SELECT 42, 'Guava'
         UNION ALL SELECT 43, 'Custard Apple'
         UNION ALL SELECT 44, 'Orange / Citrus'
         UNION ALL SELECT 45, 'Strawberry'
         UNION ALL SELECT 46, 'Other'
     ) x
WHERE f.code = 'AGRO_FARMER_VISIT'
  AND ff.field_key = 'crop';


-- =========================================================
-- AGRO NURSERY - nurseryType
-- =========================================================

INSERT INTO field_options
(
    field_id,
    label,
    value,
    display_order,
    active
)
SELECT ff.id, x.label, x.label, x.display_order, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
         JOIN
     (
         SELECT 1 display_order, 'Vegetable' label
         UNION ALL SELECT 2, 'Fruit'
         UNION ALL SELECT 3, 'Flower'
         UNION ALL SELECT 4, 'Forestry'
         UNION ALL SELECT 5, 'Ornamental'
         UNION ALL SELECT 6, 'Medicinal/Aromatic'
         UNION ALL SELECT 7, 'Plantation Crop'
         UNION ALL SELECT 8, 'Other'
     ) x
WHERE f.code = 'AGRO_NURSERY_VISIT'
  AND ff.field_key = 'nurseryType';


-- =========================================================
-- POULTRY BRANCH - birdSegment
-- =========================================================

INSERT INTO field_options
(
    field_id,
    label,
    value,
    display_order,
    active
)
SELECT ff.id, x.label, x.label, x.display_order, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
         JOIN
     (
         SELECT 1 display_order, 'Layers' label
         UNION ALL SELECT 2, 'Broilers'
         UNION ALL SELECT 3, 'Breeders'
     ) x
WHERE f.code = 'POULTRY_BRANCH_VISIT'
  AND ff.field_key = 'birdSegment';


-- =========================================================
-- POULTRY BRANCH PERSON - designation
-- =========================================================

INSERT INTO field_options
(
    field_id,
    label,
    value,
    display_order,
    active
)
SELECT ff.id, x.label, x.label, x.display_order, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
         JOIN
     (
         SELECT 1 display_order, 'Branch Manager' label
         UNION ALL SELECT 2, 'Line Supervisor'
         UNION ALL SELECT 3, 'Other'
     ) x
WHERE f.code = 'POULTRY_BRANCH_PERSON_VISIT'
  AND ff.field_key = 'designation';


-- =========================================================
-- POULTRY DOCTOR - doctorType
-- =========================================================

INSERT INTO field_options
(
    field_id,
    label,
    value,
    display_order,
    active
)
SELECT ff.id, x.label, x.label, x.display_order, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
         JOIN
     (
         SELECT 1 display_order, 'Company' label
         UNION ALL SELECT 2, 'Private Consultant'
     ) x
WHERE f.code = 'POULTRY_DOCTOR_VISIT'
  AND ff.field_key = 'doctorType';


-- =========================================================
-- POULTRY CORPORATE OFFICE - businessType
-- =========================================================

INSERT INTO field_options
(
    field_id,
    label,
    value,
    display_order,
    active
)
SELECT ff.id, x.label, x.label, x.display_order, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
         JOIN
     (
         SELECT 1 display_order, 'Integrator' label
         UNION ALL SELECT 2, 'Feed Company / Feed Mill'
         UNION ALL SELECT 3, 'Breeder'
         UNION ALL SELECT 4, 'Hatchery'
         UNION ALL SELECT 5, 'Poultry Farm'
         UNION ALL SELECT 6, 'Poultry Processing Company'
         UNION ALL SELECT 7, 'Soy / Feed Raw Material Company'
         UNION ALL SELECT 8, 'Veterinary / Poultry Health Company'
         UNION ALL SELECT 9, 'Other'
     ) x
WHERE f.code = 'POULTRY_CORPORATE_OFFICE_VISIT'
  AND ff.field_key = 'businessType';


-- =========================================================
-- POULTRY CORPORATE OFFICE PERSON - decisionMaker
-- =========================================================

INSERT INTO field_options
(
    field_id,
    label,
    value,
    display_order,
    active
)
SELECT ff.id, 'Yes', 'Yes', 1, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
WHERE f.code = 'POULTRY_CORPORATE_OFFICE_PERSON_VISIT'
  AND ff.field_key = 'decisionMaker'
UNION ALL
SELECT ff.id, 'No', 'No', 2, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
WHERE f.code = 'POULTRY_CORPORATE_OFFICE_PERSON_VISIT'
  AND ff.field_key = 'decisionMaker';


-- =========================================================
-- POULTRY CORPORATE OFFICE PERSON - decisionArea
-- =========================================================

INSERT INTO field_options
(
    field_id,
    label,
    value,
    display_order,
    active
)
SELECT ff.id, x.label, x.label, x.display_order, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
         JOIN
     (
         SELECT 1 display_order, 'Product Approval' label
         UNION ALL SELECT 2, 'Product Trial'
         UNION ALL SELECT 3, 'Purchase'
         UNION ALL SELECT 4, 'Payment'
         UNION ALL SELECT 5, 'Order'
         UNION ALL SELECT 6, 'Other'
     ) x
WHERE f.code = 'POULTRY_CORPORATE_OFFICE_PERSON_VISIT'
  AND ff.field_key = 'decisionArea';


-- =========================================================
-- POULTRY DEALER - dealerType
-- =========================================================

INSERT INTO field_options
(
    field_id,
    label,
    value,
    display_order,
    active
)
SELECT ff.id, x.label, x.label, x.display_order, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
         JOIN
     (
         SELECT 1 display_order, 'Retailer' label
         UNION ALL SELECT 2, 'Distributor'
         UNION ALL SELECT 3, 'Stockist'
         UNION ALL SELECT 4, 'Wholesaler'
         UNION ALL SELECT 5, 'Other'
     ) x
WHERE f.code = 'POULTRY_DEALER_VISIT'
  AND ff.field_key = 'dealerType';


-- =========================================================
-- POULTRY FARMER - type
-- =========================================================

INSERT INTO field_options
(
    field_id,
    label,
    value,
    display_order,
    active
)
SELECT ff.id, x.label, x.label, x.display_order, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
         JOIN
     (
         SELECT 1 display_order, 'Broiler' label
         UNION ALL SELECT 2, 'Layer'
         UNION ALL SELECT 3, 'Breeder'
     ) x
WHERE f.code = 'POULTRY_FARMER_VISIT'
  AND ff.field_key = 'type';


-- =========================================================
-- FMCG DEALER - dealerType
-- =========================================================

INSERT INTO field_options
(
    field_id,
    label,
    value,
    display_order,
    active
)
SELECT ff.id, x.label, x.label, x.display_order, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
         JOIN
     (
         SELECT 1 display_order, 'Retailer' label
         UNION ALL SELECT 2, 'Chemist'
         UNION ALL SELECT 3, 'Wholesaler'
         UNION ALL SELECT 4, 'Distributor'
         UNION ALL SELECT 5, 'Stockist'
         UNION ALL SELECT 6, 'Supermarket'
         UNION ALL SELECT 7, 'C&F'
         UNION ALL SELECT 8, 'Other'
     ) x
WHERE f.code = 'FMCG_DEALER_VISIT'
  AND ff.field_key = 'dealerType';


-- =========================================================
-- FMCG CORPORATE - businessType
-- =========================================================

INSERT INTO field_options
(
    field_id,
    label,
    value,
    display_order,
    active
)
SELECT ff.id, x.label, x.label, x.display_order, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
         JOIN
     (
         SELECT 1 display_order, 'Pharma' label
         UNION ALL SELECT 2, 'FMCG Product'
         UNION ALL SELECT 3, 'Hospital'
         UNION ALL SELECT 4, 'Supermart'
         UNION ALL SELECT 5, 'Other'
     ) x
WHERE f.code = 'FMCG_CORPORATE_VISIT'
  AND ff.field_key = 'businessType';


-- =========================================================
-- FMCG CORPORATE PERSON - department
-- =========================================================

INSERT INTO field_options
(
    field_id,
    label,
    value,
    display_order,
    active
)
SELECT ff.id, x.label, x.label, x.display_order, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
         JOIN
     (
         SELECT 1 display_order, 'Purchase' label
         UNION ALL SELECT 2, 'Payment'
         UNION ALL SELECT 3, 'Other'
     ) x
WHERE f.code = 'FMCG_CORPORATE_PERSON_VISIT'
  AND ff.field_key = 'department';


-- =========================================================
-- FMCG CORPORATE PERSON - decisionMaker
-- =========================================================

INSERT INTO field_options
(
    field_id,
    label,
    value,
    display_order,
    active
)
SELECT ff.id, 'Yes', 'Yes', 1, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
WHERE f.code = 'FMCG_CORPORATE_PERSON_VISIT'
  AND ff.field_key = 'decisionMaker'
UNION ALL
SELECT ff.id, 'No', 'No', 2, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
WHERE f.code = 'FMCG_CORPORATE_PERSON_VISIT'
  AND ff.field_key = 'decisionMaker';


-- =========================================================
-- FMCG CORPORATE PERSON - decisionArea
-- =========================================================

INSERT INTO field_options
(
    field_id,
    label,
    value,
    display_order,
    active
)
SELECT ff.id, x.label, x.label, x.display_order, TRUE
FROM form_fields ff
         JOIN forms f ON f.id = ff.form_id
         JOIN
     (
         SELECT 1 display_order, 'Product Approval' label
         UNION ALL SELECT 2, 'Product Trial'
         UNION ALL SELECT 3, 'Purchase'
         UNION ALL SELECT 4, 'Payment'
         UNION ALL SELECT 5, 'Order'
         UNION ALL SELECT 6, 'Other'
     ) x
WHERE f.code = 'FMCG_CORPORATE_PERSON_VISIT'
  AND ff.field_key = 'decisionArea';


-- =========================================================
-- END V7
-- =========================================================