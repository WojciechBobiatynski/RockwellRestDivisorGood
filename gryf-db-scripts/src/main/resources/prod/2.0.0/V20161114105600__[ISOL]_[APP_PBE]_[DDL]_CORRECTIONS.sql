CREATE TABLE APP_PBE.CORRECTIONS
(
	ID NUMBER NOT NULL,
	E_RMBS_ID NUMBER NOT NULL,
	REASON VARCHAR(100),
	COMPLEMENT_DATE DATE,
	VERSION NUMBER NOT NULL,    -- Standardowa kolumna wersji na potrzeby optymistycznego blokowania (Gryf)
	CREATED_USER VARCHAR(100) NOT NULL,    -- U¿ytkownik tworzacy wiersz - kolumna audytowa
	CREATED_TIMESTAMP TIMESTAMP(6) NOT NULL,    -- Timestamp utworzenia wiersza - kolumna audytowa
	MODIFIED_USER VARCHAR2(100) NOT NULL,    -- Ostatni u¿ytkownik modyfikuj¹cy wiersz - kolumna audytowa
	MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL    -- Timestamp ostatniej modyfikacji wiersza - kolumna audytowa
);

COMMENT ON TABLE APP_PBE.CORRECTIONS IS '@Author(Adam Kmieciñski); @Project(Gryf-PBE); @Date(2016-11-14);@Purpose(Tabela przechowuj¹ca dane z korektami do e-rozliczeñ);';

COMMENT ON COLUMN APP_PBE.CORRECTIONS.ID IS 'Klucz g³ówny. Identyfikator korekty. Sekwencja CORREC_SEQ';
COMMENT ON COLUMN APP_PBE.CORRECTIONS.E_RMBS_ID IS 'E rozliczenie. Klucz obcy do E_REIMBURSEMENTS';
COMMENT ON COLUMN APP_PBE.CORRECTIONS.REASON IS 'Powód korekty';
COMMENT ON COLUMN APP_PBE.CORRECTIONS.COMPLEMENT_DATE IS 'Data uzupe³nienia korekty';
COMMENT ON COLUMN APP_PBE.CORRECTIONS.VERSION IS 'Standardowa kolumna wersji na potrzeby optymistycznego blokowania (Gryf)';
COMMENT ON COLUMN APP_PBE.CORRECTIONS.CREATED_USER IS 'U¿ytkownik tworzacy wiersz - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.CORRECTIONS.CREATED_TIMESTAMP IS 'Timestamp utworzenia wiersza - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.CORRECTIONS.MODIFIED_USER IS 'Ostatni u¿ytkownik modyfikuj¹cy wiersz - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.CORRECTIONS.MODIFIED_TIMESTAMP IS 'Timestamp ostatniej modyfikacji wiersza - kolumna audytowa';

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'CORRECTIONS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'CORRECTIONS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'CORRECTIONS', 'SELECT'));
END;
/

-- Indeksy
CREATE UNIQUE INDEX APP_PBE.PK_CORRECTIONS_ID ON APP_PBE.CORRECTIONS (ID);

ALTER TABLE APP_PBE.CORRECTIONS
 ADD CONSTRAINT PK_CORRECTIONS_ID
	PRIMARY KEY (ID);

ALTER TABLE APP_PBE.CORRECTIONS
 ADD CONSTRAINT FK_CORRECTIONS_E_RMBS_ID
	FOREIGN KEY (E_RMBS_ID) REFERENCES APP_PBE.E_REIMBURSEMENTS (ID);

--------------------------------------------SEQUENCE--------------------------------------------
CREATE SEQUENCE EAGLE.CORREC_SEQ
MINVALUE 1
MAXVALUE 9999999999999999999999999999
INCREMENT BY 1
START WITH 100
CACHE 20
NOORDER NOCYCLE;

-- uprawnienia
BEGIN
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE', 'CORREC_SEQ', 'ALL'));
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'DEVELOPER', 'CORREC_SEQ', 'SELECT'));
END;
/
--------------------------------------------------------------------------------------------------