

CREATE TABLE APP_PBE.CONTRACT_TRAINING_CATEGORIES
(
  CONTRACT_ID NUMBER NOT NULL,
  TRAINING_CATEGORY_ID VARCHAR2(5  BYTE) NOT NULL
);

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'CONTRACT_TRAINING_CATEGORIES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'CONTRACT_TRAINING_CATEGORIES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'CONTRACT_TRAINING_CATEGORIES', 'SELECT'));
END;
/

-- Klucze
CREATE UNIQUE INDEX APP_PBE.CON_TRA_CAT_PK ON APP_PBE.CONTRACT_TRAINING_CATEGORIES (CONTRACT_ID, TRAINING_CATEGORY_ID);
ALTER TABLE APP_PBE.CONTRACT_TRAINING_CATEGORIES ADD CONSTRAINT CON_TRA_CAT_PK PRIMARY KEY (CONTRACT_ID, TRAINING_CATEGORY_ID);

-- Klucze obce
ALTER TABLE APP_PBE.CONTRACT_TRAINING_CATEGORIES ADD CONSTRAINT CON_TRA_CAT_CON_FK FOREIGN KEY (CONTRACT_ID) REFERENCES APP_PBE.CONTRACTS (ID);
ALTER TABLE APP_PBE.CONTRACT_TRAINING_CATEGORIES ADD CONSTRAINT CON_TRA_CAT_TRA_CAT_FK FOREIGN KEY (TRAINING_CATEGORY_ID) REFERENCES APP_PBE.TI_TRAINING_CATEGORIES (ID);

-- Komentarze
COMMENT ON TABLE APP_PBE.CONTRACT_TRAINING_CATEGORIES IS '@Author(Tomasz Bilski); @Project(GryfBonEl); @Date(2016-11-05);@Purpose(Tabel ��cz�ca umow� oraz kategorie szkolenia);';
COMMENT ON COLUMN APP_PBE.CONTRACT_TRAINING_CATEGORIES.CONTRACT_ID IS 'Identyfikator umowy';
COMMENT ON COLUMN APP_PBE.CONTRACT_TRAINING_CATEGORIES.TRAINING_CATEGORY_ID IS 'Kategoria szkolenia';
