CREATE TABLE APP_PBE.E_REIMBURSEMENTS
(
	ID NUMBER NOT NULL,    -- Identyfikator rozliczenia elektronicznego. Klucz g³ówny
	TI_TR_INST_ID NUMBER NOT NULL,    -- Klucz obcy do TI_TRAINING_INSTANCES
	STATUS_ID VARCHAR2(5) NOT NULL,    -- Status rozliczenia. Klucz obcy do E_REIMBURSEMENT_STATUSES
	REIMBURSEMENT_DATE DATE,    -- Data rozliczenia (wykonania przelewu)
  SXO_TI_AMOUNT_DUE_TOTAL NUMBER, -- Ca³kowita kwota od Operatora Finansowego dla Instytucji szkoleniowej
  IND_TI_AMOUNT_DUE_TOTAL NUMBER, -- Ca³kowtia kwota od U¿ytkownika dla instytucji szkoleniowej
  TI_REIMB_ACCOUNT_NUMBER VARCHAR(26), -- Numer konta instytucji szkoleniowej do zwrotu
  REQUIRED_CORRECTION_DATE DATE, -- Wymagana data otrzymania korekty dla faktury
	VERSION NUMBER NOT NULL,    -- Standardowa kolumna wersji na potrzeby optymistycznego blokowania (Gryf)
	CREATED_USER VARCHAR(100) NOT NULL,    -- U¿ytkownik tworzacy wiersz - kolumna audytowa
	CREATED_TIMESTAMP TIMESTAMP(6) NOT NULL,    -- Timestamp utworzenia wiersza - kolumna audytowa
	MODIFIED_USER VARCHAR2(100) NOT NULL,    -- Ostatni u¿ytkownik modyfikuj¹cy wiersz - kolumna audytowa
	MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL    -- Timestamp ostatniej modyfikacji wiersza - kolumna audytowa
);

COMMENT ON TABLE APP_PBE.E_REIMBURSEMENTS IS '@Author(Adam Kmieciñski); @Project(Gryf-PBE); @Date(2016-11-14);@Purpose(Tabela przechowuj¹ca dane z e-rozliczeniami);';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.ID IS 'Identyfikator rozliczenia elektronicznego. Klucz g³ówny. Sekwencja PBE_E_REIMB_SEQ';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.TI_TR_INST_ID IS 'Klucz obcy do TI_TRAINING_INSTANCES';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.STATUS_ID IS 'Status rozliczenia. Klucz obcy do E_REIMBURSEMENT_STATUSES';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.REIMBURSEMENT_DATE IS 'Data rozliczenia (wykonania przelewu)';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.SXO_TI_AMOUNT_DUE_TOTAL IS 'Ca³kowita kwota od Operatora Finansowego dla Instytucji szkoleniowej';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.IND_TI_AMOUNT_DUE_TOTAL IS 'Ca³kowtia kwota od U¿ytkownika dla Instytucji szkoleniowej';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.TI_REIMB_ACCOUNT_NUMBER IS 'Numer konta instytucji szkoleniowej do zwrotu';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.REQUIRED_CORRECTION_DATE IS 'Wymagana data otrzymania korekty dla faktury';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.VERSION IS 'Standardowa kolumna wersji na potrzeby optymistycznego blokowania (Gryf)';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.CREATED_USER IS 'U¿ytkownik tworzacy wiersz - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.CREATED_TIMESTAMP IS 'Timestamp utworzenia wiersza - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.MODIFIED_USER IS 'Ostatni u¿ytkownik modyfikuj¹cy wiersz - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.MODIFIED_TIMESTAMP IS 'Timestamp ostatniej modyfikacji wiersza - kolumna audytowa';

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'E_REIMBURSEMENTS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'E_REIMBURSEMENTS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'E_REIMBURSEMENTS', 'SELECT'));
END;
/

-- Indeksy
CREATE UNIQUE INDEX APP_PBE.PK_E_REIMBURSEMENTS_ID ON APP_PBE.E_REIMBURSEMENTS (ID);

ALTER TABLE APP_PBE.E_REIMBURSEMENTS
 ADD CONSTRAINT PK_E_REIMBURSEMENTS_ID
	PRIMARY KEY (ID);

ALTER TABLE APP_PBE.E_REIMBURSEMENTS
 ADD CONSTRAINT FK_E_RMBS_ST_ID
	FOREIGN KEY (STATUS_ID) REFERENCES APP_PBE.E_REIMBURSEMENT_STATUSES (ID);

ALTER TABLE APP_PBE.E_REIMBURSEMENTS
 ADD CONSTRAINT FK_E_RMBS_TI_TR_ID
	FOREIGN KEY (TI_TR_INST_ID) REFERENCES APP_PBE.TI_TRAINING_INSTANCES (ID);

ALTER TABLE APP_PBE.E_REIMBURSEMENTS
 ADD CONSTRAINT E_RMBS_TI_TR_ID_UIX UNIQUE (TI_TR_INST_ID);

--------------------------------------------SEQUENCE--------------------------------------------
CREATE SEQUENCE EAGLE.PBE_E_REIMB_SEQ
MINVALUE 1
MAXVALUE 9999999999999999999999999999
INCREMENT BY 1
START WITH 100
CACHE 20
NOORDER NOCYCLE;

-- uprawnienia
BEGIN
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE', 'PBE_E_REIMB_SEQ', 'ALL'));
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'DEVELOPER', 'PBE_E_REIMB_SEQ', 'SELECT'));
END;
/
--------------------------------------------------------------------------------------------------