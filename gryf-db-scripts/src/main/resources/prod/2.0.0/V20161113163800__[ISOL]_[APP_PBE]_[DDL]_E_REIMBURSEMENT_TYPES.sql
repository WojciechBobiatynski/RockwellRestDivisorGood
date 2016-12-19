CREATE TABLE APP_PBE.E_REIMBURSEMENT_TYPES
(
	CODE VARCHAR2(10) NOT NULL,    -- Kod typu rozliczenia. Klucz g³ówny
	NAME VARCHAR2(200) NOT NULL,    -- Nazwa typu rozliczenia
	ORDINAL NUMBER    -- Liczba porz¹dkowa
);

/* Create Comments, Sequences and Triggers for Autonumber Columns */

COMMENT ON TABLE APP_PBE.E_REIMBURSEMENT_TYPES IS '@Author(Adam Kmieciñski); @Project(GryfBonEl); @Date(2016-12-09);@Purpose(Tabela przechowuj¹ca typy rozliczenia bonów elektronicznych);';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENT_TYPES.CODE IS 'Kod typu rozliczenia. Klucz g³ówny';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENT_TYPES.NAME IS 'Nazwa typu rozliczenia';
COMMENT ON COLUMN APP_PBE.E_REIMBURSEMENT_TYPES.ORDINAL IS 'Liczba porz¹dkowa';

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'E_REIMBURSEMENT_TYPES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'E_REIMBURSEMENT_TYPES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'E_REIMBURSEMENT_TYPES', 'SELECT'));
END;
/

-- Indeksy
CREATE UNIQUE INDEX APP_PBE.E_REIMBURSEM_TYPE_PK ON APP_PBE.E_REIMBURSEMENT_TYPES (CODE);

ALTER TABLE APP_PBE.E_REIMBURSEMENT_TYPES 
 ADD CONSTRAINT E_REIMBURSEM_TYPE_PK
	PRIMARY KEY (CODE);