CREATE TABLE APP_PBE.E_REIMBURSEMENTS
(
	ID NUMBER NOT NULL,    -- Identyfikator rozliczenia elektronicznego. Klucz g��wny
	TYPE_ID VARCHAR2(10) NOT NULL,   -- Typ rozliczenia.Klucz obcy do E_REIMBURSEMENT_TYPES
	TI_TR_INST_ID NUMBER,    -- Klucz obcy do TI_TRAINING_INSTANCES
	PRODUCT_INSTANCE_POOL_ID NUMBER,  -- Rozliczana p�la bon�w.Klucz obcy do PRODUCT_INSTANCE_POOLS
	STATUS_ID VARCHAR2(5) NOT NULL,    -- Status rozliczenia. Klucz obcy do E_REIMBURSEMENT_STATUSES
	ARRIVAL_DATE DATE,    -- Data wys�ania rozliczenia do weryfikacji
	REIMBURSEMENT_DATE DATE,    -- Data rozliczenia (wykonania przelewu)
	RECON_DATE DATE,    -- Data zatwierdzenia rozliczenia
  SXO_TI_AMOUNT_DUE_TOTAL NUMBER, -- Ca�kowita kwota od Operatora Finansowego dla Instytucji szkoleniowej
  SXO_IND_AMOUNT_DUE_TOTAL NUMBER, -- Kwota nadp�aty wk�adu w�asnego u�ytkownika - zwrot od OF
  IND_TI_AMOUNT_DUE_TOTAL NUMBER, -- Ca�kowtia kwota od U�ytkownika dla instytucji szkoleniowej
  IND_OWN_CONTRIBUTION_USED NUMBER, --Wykorzysany wk�ad w�asny uczestnika
  IND_SUBSIDY_VALUE NUMBER, --Warto�� dofinansowania u�ytkownika
  TI_REIMB_ACCOUNT_NUMBER VARCHAR(26), -- Numer konta instytucji szkoleniowej do zwrotu
  REQUIRED_CORRECTION_DATE DATE, -- Wymagana data otrzymania korekty dla faktury
	VERSION NUMBER NOT NULL,    -- Standardowa kolumna wersji na potrzeby optymistycznego blokowania (Gryf)
	CREATED_USER VARCHAR(100) NOT NULL,    -- U�ytkownik tworzacy wiersz - kolumna audytowa
	CREATED_TIMESTAMP TIMESTAMP(6) NOT NULL,    -- Timestamp utworzenia wiersza - kolumna audytowa
	MODIFIED_USER VARCHAR2(100) NOT NULL,    -- Ostatni u�ytkownik modyfikuj�cy wiersz - kolumna audytowa
	MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL    -- Timestamp ostatniej modyfikacji wiersza - kolumna audytowa
);

COMMENT ON TABLE APP_PBE.E_REIMBURSEMENTS IS '@Author(Adam Kmieci�ski); @Project(Gryf-PBE); @Date(2016-11-14);@Purpose(Tabela przechowuj�ca dane z e-rozliczeniami);';

COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.ID IS 'Identyfikator rozliczenia elektronicznego. Klucz g��wny. Sekwencja PBE_E_REIMB_SEQ';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.TYPE_ID IS 'Typ rozliczenia.Klucz obcy do E_REIMBURSEMENT_TYPES';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.TI_TR_INST_ID IS 'Klucz obcy do TI_TRAINING_INSTANCES';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.PRODUCT_INSTANCE_POOL_ID IS 'Rozliczana pula bon�w.Klucz obcy do PRODUCT_INSTANCE_POOLS';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.STATUS_ID IS 'Status rozliczenia. Klucz obcy do E_REIMBURSEMENT_STATUSES';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.ARRIVAL_DATE IS 'Data wys�ania rozliczenia do weryfikacji';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.REIMBURSEMENT_DATE IS 'Data rozliczenia (wykonania przelewu)';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.RECON_DATE IS 'Data zatwierdzenia rozliczenia';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.SXO_TI_AMOUNT_DUE_TOTAL IS 'Ca�kowita kwota od Operatora Finansowego dla Instytucji szkoleniowej';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.SXO_IND_AMOUNT_DUE_TOTAL IS 'Ca�kowita kwota od Operatora Finansowego dla U�ytkownika - kwota nadp�aty wk�adu w�asnego';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.IND_TI_AMOUNT_DUE_TOTAL IS 'Ca�kowtia kwota od U�ytkownika dla Instytucji szkoleniowej';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.IND_OWN_CONTRIBUTION_USED IS 'Wykorzysany wk�ad w�asny U�ytkownika';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.IND_SUBSIDY_VALUE IS 'Warto�� dofinansowania U�ytkownika';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.TI_REIMB_ACCOUNT_NUMBER IS 'Numer konta instytucji szkoleniowej do zwrotu';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.REQUIRED_CORRECTION_DATE IS 'Wymagana data otrzymania korekty dla faktury';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.VERSION IS 'Standardowa kolumna wersji na potrzeby optymistycznego blokowania (Gryf)';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.CREATED_USER IS 'U�ytkownik tworzacy wiersz - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.CREATED_TIMESTAMP IS 'Timestamp utworzenia wiersza - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENTS.MODIFIED_USER IS 'Ostatni u�ytkownik modyfikuj�cy wiersz - kolumna audytowa';
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
 ADD CONSTRAINT FK_E_RMBS_E_RMBS_TYPE
	FOREIGN KEY (TYPE_ID) REFERENCES APP_PBE.E_REIMBURSEMENT_TYPES (CODE)
;

ALTER TABLE APP_PBE.E_REIMBURSEMENTS 
 ADD CONSTRAINT FK_E_RMBS_POOL_ID
	FOREIGN KEY (PRODUCT_INSTANCE_POOL_ID) REFERENCES APP_PBE.PBE_PRODUCT_INSTANCE_POOLS (ID)
;

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