CREATE TABLE APP_PBE.CORRECTION_ATTACHMENTS
(
	ID NUMBER NOT NULL,    -- Identyfiaktor za³¹cznika korekty. Klucz g³ówny. Sekwencja EAGLE.CORREC_ATT_SEQ
	CORR_ID NUMBER NOT NULL,    -- Id korekty. Klucz obcy do APP_PBE.CORRECTIONS
	E_RMBS_ATT_ID NUMBER NOT NULL,    -- Id zmienianego za³¹cznika rozliczenia. Klucz obcy do APP_PBE.E_RMBS_ATTACHMENTS
	FILE_ID NUMBER,
	OLD_DOCUMENT_NUMBER VARCHAR2(200),    -- Pierwotny numer dokumentu za³¹cznika, który zmieniliœmy w ramach korekty
	OLD_ADDITIONAL_DESC VARCHAR2(200),    -- Pierwotny dodatkowy opis za³¹cznika, który zmieniliœmy w ramach korekty
	VERSION NUMBER NOT NULL,
	CREATED_USER VARCHAR2(100) NOT NULL,
	CREATED_TIMESTAMP TIMESTAMP(6) NOT NULL,
	MODIFIED_USER VARCHAR2(100) NOT NULL,
	MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL
)
;

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'CORRECTION_ATTACHMENTS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'CORRECTION_ATTACHMENTS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'CORRECTION_ATTACHMENTS', 'SELECT'));
END;
/

-- Indeksy
CREATE UNIQUE INDEX APP_PBE.CORR_ATT_PK ON APP_PBE.CORRECTION_ATTACHMENTS (ID);

/* Create Primary Keys, Indexes, Uniques, Checks, Triggers */

ALTER TABLE APP_PBE.CORRECTION_ATTACHMENTS
 ADD CONSTRAINT CORR_ATT_PK
	PRIMARY KEY (ID);

/* Create Foreign Key Constraints */

ALTER TABLE APP_PBE.CORRECTION_ATTACHMENTS
 ADD CONSTRAINT CORR_ATT_CORR_FK
	FOREIGN KEY (CORR_ID) REFERENCES APP_PBE.CORRECTIONS (ID);

ALTER TABLE APP_PBE.CORRECTION_ATTACHMENTS
 ADD CONSTRAINT CORR_ATT_ERMBS_ATT_FK
	FOREIGN KEY (E_RMBS_ATT_ID) REFERENCES APP_PBE.E_RMBS_ATTACHMENTS (ID);

/* Create Comments, Sequences and Triggers for Autonumber Columns */

COMMENT ON TABLE APP_PBE.CORRECTION_ATTACHMENTS IS '@Author(Adam Kmieciñski); @Project(Gryf-PBE); @Date(2016-12-05) ;@Purpose(Tabela dla za³¹czników do korekt);';

COMMENT ON COLUMN APP_PBE.CORRECTION_ATTACHMENTS.ID IS 'Identyfiaktor za³¹cznika korekty. Klucz g³ówny. Sekwencja EAGLE.CORREC_ATT_SEQ';
COMMENT ON COLUMN APP_PBE.CORRECTION_ATTACHMENTS.CORR_ID IS 'Id korekty. Klucz obcy do APP_PBE.CORRECTIONS';
COMMENT ON COLUMN APP_PBE.CORRECTION_ATTACHMENTS.E_RMBS_ATT_ID IS 'Id zmienianego za³¹cznika rozliczenia. Klucz obcy do APP_PBE.E_RMBS_ATTACHMENTS';
COMMENT ON COLUMN APP_PBE.CORRECTION_ATTACHMENTS.FILE_ID IS 'Id do rekordu z informacjami o zapisanym pliku.';
COMMENT ON COLUMN APP_PBE.CORRECTION_ATTACHMENTS.OLD_DOCUMENT_NUMBER IS 'Pierwotny numer dokumentu za³¹cznika, który zmieniliœmy w ramach korekty';
COMMENT ON COLUMN APP_PBE.CORRECTION_ATTACHMENTS.OLD_ADDITIONAL_DESC IS 'Pierwotny dodatkowy opis za³¹cznika, który zmieniliœmy w ramach korekty';
COMMENT ON COLUMN APP_PBE.CORRECTION_ATTACHMENTS.VERSION IS 'Standardowa kolumna wersji na potrzeby optymistycznego blokowania (Gryf)';
COMMENT ON COLUMN APP_PBE.CORRECTION_ATTACHMENTS.CREATED_USER IS 'U¿ytkownik tworzacy wiersz - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.CORRECTION_ATTACHMENTS.CREATED_TIMESTAMP IS 'Timestamp utworzenia wiersza - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.CORRECTION_ATTACHMENTS.MODIFIED_USER IS 'Ostatni u¿ytkownik modyfikuj¹cy wiersz - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.CORRECTION_ATTACHMENTS.MODIFIED_TIMESTAMP IS 'Timestamp ostatniej modyfikacji wiersza - kolumna audytowa';

--------------------------------------------SEQUENCE--------------------------------------------
CREATE SEQUENCE EAGLE.CORREC_ATT_SEQ
MINVALUE 1
MAXVALUE 9999999999999999999999999999
INCREMENT BY 1
START WITH 1
CACHE 20
NOORDER NOCYCLE;

-- uprawnienia
BEGIN
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE', 'CORREC_ATT_SEQ', 'ALL'));
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'DEVELOPER', 'CORREC_ATT_SEQ', 'SELECT'));
END;
/
--------------------------------------------------------------------------------------------------
