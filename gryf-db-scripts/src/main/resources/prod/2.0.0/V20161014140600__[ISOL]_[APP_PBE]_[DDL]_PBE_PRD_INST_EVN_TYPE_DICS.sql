CREATE TABLE APP_PBE.PBE_PRODUCT_INSTANCE_EVE_TYPES
(
	ID VARCHAR2(5) NOT NULL,
	NAME VARCHAR2(200) NOT NULL,
	ORDINAL NUMBER(10) NOT NULL
);

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'PBE_PRODUCT_INSTANCE_EVE_TYPES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'PBE_PRODUCT_INSTANCE_EVE_TYPES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'PBE_PRODUCT_INSTANCE_EVE_TYPES', 'SELECT'));
END;
/

-- Klucze
CREATE UNIQUE INDEX APP_PBE.PRD_INST_EVN_TYP_DIC_PK ON APP_PBE.PBE_PRODUCT_INSTANCE_EVE_TYPES (ID);
ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCE_EVE_TYPES ADD CONSTRAINT PRD_INST_EVN_TYP_DIC_PK PRIMARY KEY (ID);

-- Komentarze
COMMENT ON TABLE APP_PBE.PBE_PRODUCT_INSTANCE_EVE_TYPES IS '@Author(Adam Kmieci�ski); @Project(GryfBonEl); @Date(2016-10-12);@Purpose(Tabel warto�ci s�ownikowych dla typ�w wydarze� instancji produkt�w);';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_EVE_TYPES.ID IS 'Kod s�ownika. Klucz g��wny';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_EVE_TYPES.NAME IS 'Opis warto�ci s�ownikowej';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_EVE_TYPES.ORDINAL IS 'Liczba porz�dkowa';