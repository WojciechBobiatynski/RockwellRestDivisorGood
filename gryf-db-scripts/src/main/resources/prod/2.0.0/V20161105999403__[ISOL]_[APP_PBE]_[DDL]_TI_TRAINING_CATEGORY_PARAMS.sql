

CREATE TABLE APP_PBE.TI_TRAINING_CATEGORY_PARAMS
(
  ID NUMBER NOT NULL,
  CATEGORY_ID VARCHAR2(5  BYTE) NOT NULL,
  GRANT_PROGRAM_ID NUMBER NOT NULL,
  PRODUCT_INSTANCE_FOR_HOUR NUMBER(10),
  MAX_PRODUCT_INSTANCE NUMBER(10),
  DATE_FROM DATE,
  DATE_TO DATE
);

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'TI_TRAINING_CATEGORY_PARAMS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'TI_TRAINING_CATEGORY_PARAMS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'TI_TRAINING_CATEGORY_PARAMS', 'SELECT'));
END;
/

-- Klucze
CREATE UNIQUE INDEX APP_PBE.TI_TRA_CAT_PAR_PK ON APP_PBE.TI_TRAINING_CATEGORY_PARAMS (ID);
ALTER TABLE APP_PBE.TI_TRAINING_CATEGORY_PARAMS ADD CONSTRAINT TI_TRA_CAT_PAR_PK PRIMARY KEY (ID);

-- Klucze obce
ALTER TABLE APP_PBE.TI_TRAINING_CATEGORY_PARAMS ADD CONSTRAINT TI_TRA_CAT_CAT_GR_PR_CATEG_FK FOREIGN KEY (CATEGORY_ID) REFERENCES APP_PBE.TI_TRAINING_CATEGORIES (ID);
ALTER TABLE APP_PBE.TI_TRAINING_CATEGORY_PARAMS ADD CONSTRAINT TI_TRA_CAT_CA_GR_PR_GRA_PR_FK FOREIGN KEY (GRANT_PROGRAM_ID) REFERENCES APP_PBE.GRANT_PROGRAMS (ID);

-- Komentarze
COMMENT ON TABLE APP_PBE.TI_TRAINING_CATEGORY_PARAMS IS '@Author(Tomasz Bilski); @Project(GryfBonEl); @Date(2016-11-05);@Purpose(Tabel ??cz?ca katalog szkole? i program dofinansowania w danych datach);';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_CATEGORY_PARAMS.ID IS 'Klucz g??wny';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_CATEGORY_PARAMS.CATEGORY_ID IS 'Indetyfikator kategorii';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_CATEGORY_PARAMS.GRANT_PROGRAM_ID IS 'Indetyfikator programu dofinansowania';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_CATEGORY_PARAMS.PRODUCT_INSTANCE_FOR_HOUR IS 'Ilosc bon?w, kt?re rozliczaj? jedn? godzin? szkolenia';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_CATEGORY_PARAMS.MAX_PRODUCT_INSTANCE IS 'Maksymalna ilo?? bon?w za ca?e szkolenie';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_CATEGORY_PARAMS.DATE_FROM IS 'Data od kt?rej obowi?zywuje dany parametr';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_CATEGORY_PARAMS.DATE_TO IS 'Data do kt?rej obowi?zywuje dany parametr';
