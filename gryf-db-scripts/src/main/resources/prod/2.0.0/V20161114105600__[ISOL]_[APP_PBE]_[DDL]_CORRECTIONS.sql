CREATE TABLE APP_PBE.CORRECTIONS
(
	ID NUMBER NOT NULL,
	E_RMBS_ID NUMBER NOT NULL
);

COMMENT ON TABLE APP_PBE.CORRECTIONS IS '@Author(Adam Kmieciñski); @Project(Gryf-PBE); @Date(2016-11-14);@Purpose(Tabela przechowuj¹ca dane z korektami do e-rozliczeñ);';

COMMENT ON COLUMN APP_PBE.CORRECTIONS.ID IS 'Klucz g³ówny. Identyfikator korekty. Sekwencja CORREC_SEQ';
COMMENT ON COLUMN APP_PBE.CORRECTIONS.E_RMBS_ID IS 'E rozliczenie. Klucz obcy do E_REIMBURSEMENTS';

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