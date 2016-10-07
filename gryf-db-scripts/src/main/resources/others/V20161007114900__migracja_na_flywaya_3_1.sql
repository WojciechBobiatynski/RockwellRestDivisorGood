
DROP TABLE SCHEMA_VERSION;

--Czyszczenie po zaaplikowanych  migracjach
--DML 
DECLARE
TRIN_ID number;
USR_ID number;
BEGIN

SELECT ID, TRAINING_ISTITUTION_ID INTO  USR_ID, TRIN_ID FROM APP_PBE.TRAINING_INSTITUTION_USERS WHERE LOGIN ='trin_usr_test';

DELETE FROM APP_PBE.TRIN_USERS_IN_ROLE WHERE TRIN_USER_ID = USR_ID;
DELETE FROM APP_PBE.TRAINING_INSTITUTION_USERS WHERE ID = USR_ID;
DELETE FROM APP_PBE.TRAINING_INSTITUTIONS WHERE ID = TRIN_ID;
DELETE FROM APP_PBE.GRF_PRIVS_IN_ROLE WHERE GRF_PRIV_CODE = 'TRIN_TEST_PRIV' OR GRF_ROLE_CODE = 'TRIN_TEST_ROLE'; 
DELETE FROM APP_PBE.GRF_ROLES WHERE CODE = 'TRIN_TEST_ROLE';
DELETE FROM APP_PBE.GRF_PRIVILEGES WHERE CODE = 'TRIN_TEST_PRIV';
END;
/
--DDL
DROP TABLE APP_PBE.GRF_PRIVS_IN_ROLE;
DROP TABLE APP_PBE.TRIN_USERS_IN_ROLE;
DROP TABLE APP_PBE.TRAINING_INSTITUTION_USERS;
DROP TABLE APP_PBE.GRF_ROLES;
DROP TABLE APP_PBE.GRF_PRIVILEGES;

DROP SEQUENCE EAGLE.TRIN_USR_SEQ;
