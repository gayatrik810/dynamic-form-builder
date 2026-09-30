-- =========================================================
-- V4 - FIX GAYATRI PASSWORD
-- =========================================================

UPDATE users
SET password = '$2a$10$AxBP95BqtAAhiqeVD9KqP.Ws0rcVc/lL53LpFUSuoCAwo1prBZ5Wy'
WHERE email = 'gayatri@test.com';