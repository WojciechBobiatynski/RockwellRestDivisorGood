

CREATE TABLE APP_PBE.PBE_PRODUCT_INSTANCE_E_T
(
	ID VARCHAR2(10 BYTE) NOT NULL,
	NAME VARCHAR2(200 BYTE) NOT NULL,
	ORDINAL NUMBER(10) NOT NULL,
  SOURCE_TYPE VARCHAR2(35 BYTE)
);

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'PBE_PRODUCT_INSTANCE_E_T', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'PBE_PRODUCT_INSTANCE_E_T', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'PBE_PRODUCT_INSTANCE_E_T', 'SELECT'));
END;
/

-- Klucze
CREATE UNIQUE INDEX APP_PBE.PBE_PRD_INS_EVE_TYP_PK ON APP_PBE.PBE_PRODUCT_INSTANCE_E_T (ID);
ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCE_E_T ADD CONSTRAINT PBE_PRD_INS_EVE_TYP_PK PRIMARY KEY (ID);

-- Komentarze
COMMENT ON TABLE APP_PBE.PBE_PRODUCT_INSTANCE_E_T IS '@Author(Tomasz Bilski); @Project(GryfBonEl); @Date(2016-11-05);@Purpose(Tabel warto�ci s�ownikowych dla typ�w zdarze� puli bon�w);';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_E_T.ID IS 'Kod s�ownika. Klucz g��wny';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_E_T.NAME IS 'Opis warto�ci s�ownikowej';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_E_T.ORDINAL IS 'Liczba porz�dkowa';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_E_T.SOURCE_TYPE IS 'Typ �r�d�a (nazwa tabelki) robi�cy zmian� na obiekcie';