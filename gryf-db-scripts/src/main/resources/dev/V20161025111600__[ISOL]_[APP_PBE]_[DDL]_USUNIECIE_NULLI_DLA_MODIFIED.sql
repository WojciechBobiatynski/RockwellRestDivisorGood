
-- INDIVIDUAL_USERS
UPDATE APP_PBE.INDIVIDUAL_USERS SET MODIFIED_USER = 'system' WHERE MODIFIED_USER IS NULL;
UPDATE APP_PBE.INDIVIDUAL_USERS SET MODIFIED_TIMESTAMP = SYSDATE WHERE MODIFIED_TIMESTAMP IS NULL;