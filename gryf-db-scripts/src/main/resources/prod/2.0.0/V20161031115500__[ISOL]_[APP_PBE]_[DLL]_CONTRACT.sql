

--drop table APP_PBE.CONTRACTS;
--drop table APP_PBE.CONTRACT_TYPES;

-----------------
--CONTRACT_TYPES--
-----------------

CREATE TABLE APP_PBE.CONTRACT_TYPES
(
	ID VARCHAR2(50) NOT NULL,
	NAME VARCHAR2(200) NOT NULL,
	ORDINAL NUMBER(10) NOT NULL
);

CREATE UNIQUE INDEX APP_PBE.CONTRACT_TYPES_PK ON APP_PBE.CONTRACT_TYPES (ID);
ALTER TABLE APP_PBE.CONTRACT_TYPES ADD CONSTRAINT CONTRACT_TYPES_PK PRIMARY KEY (ID);

COMMENT ON TABLE APP_PBE.CONTRACT_TYPES IS '@Author(Tomasz.Bilski); @Project(Gryf-PBE); @Date(2016-10-28) ;@Purpose(Tabela przechowująca typ umów);';
COMMENT ON COLUMN APP_PBE.CONTRACT_TYPES.ID IS 'Kod typu umowy - klucz gówny';
COMMENT ON COLUMN APP_PBE.CONTRACT_TYPES.NAME IS 'Nazwa';
COMMENT ON COLUMN APP_PBE.CONTRACT_TYPES.ORDINAL IS 'Kolejność przy liście wyboru';


BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'CONTRACT_TYPES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'CONTRACT_TYPES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'CONTRACT_TYPES', 'SELECT'));
END;
/




-------------
--CONTRACTS--
-------------

CREATE TABLE APP_PBE.CONTRACTS
(
	ID NUMBER(8,2) NOT NULL,
	CONTRACT_TYPE_ID VARCHAR2(50) NOT NULL,
	INDIVIDUAL_ID NUMBER(8,2),
	ENTERPRISE_ID NUMBER(8,2),
	GRANT_PROGRAM_ID NUMBER(8,2),
	SIGN_DATE DATE,
	EXPIRY_DATE DATE,
	VERSION NUMBER,
	CREATED_USER VARCHAR2(100),
	CREATED_TIMESTAMP TIMESTAMP(6),
	MODIFIED_USER VARCHAR2(100),
	MODIFIED_TIMESTAMP TIMESTAMP(6)
);

CREATE UNIQUE INDEX APP_PBE.CONTRACTS_PK ON APP_PBE.CONTRACTS (ID);
ALTER TABLE APP_PBE.CONTRACTS ADD CONSTRAINT CONTRACTS_PK PRIMARY KEY (ID);

ALTER TABLE APP_PBE.CONTRACTS ADD CONSTRAINT CONT_CONTTYPE_FK FOREIGN KEY (CONTRACT_TYPE_ID) REFERENCES APP_PBE.CONTRACT_TYPES (ID);
ALTER TABLE APP_PBE.CONTRACTS ADD CONSTRAINT CONT_IND_FK FOREIGN KEY (INDIVIDUAL_ID) REFERENCES APP_PBE.INDIVIDUALS (ID);
ALTER TABLE APP_PBE.CONTRACTS ADD CONSTRAINT CONT_ENT_FK FOREIGN KEY (ENTERPRISE_ID) REFERENCES APP_PBE.ENTERPRISES (ID);
ALTER TABLE APP_PBE.CONTRACTS ADD CONSTRAINT CONT_GRPR_FK FOREIGN KEY (GRANT_PROGRAM_ID) REFERENCES APP_PBE.GRANT_PROGRAMS (ID);

COMMENT ON TABLE APP_PBE.CONTRACTS IS '@Author(Tomasz.Bilski); @Project(Gryf-PBE); @Date(2016-10-28) ;@Purpose(Tabela przechowująca umowy);';
COMMENT ON COLUMN APP_PBE.CONTRACTS.ID IS 'Identyfikator umowy';
COMMENT ON COLUMN APP_PBE.CONTRACTS.CONTRACT_TYPE_ID IS 'Typ umowy';
COMMENT ON COLUMN APP_PBE.CONTRACTS.INDIVIDUAL_ID IS 'Osoba fizyczna w która podpisuje umowę';
COMMENT ON COLUMN APP_PBE.CONTRACTS.ENTERPRISE_ID IS 'MŚP w ramach którego jest zawarta umowa';
COMMENT ON COLUMN APP_PBE.CONTRACTS.GRANT_PROGRAM_ID IS 'Program dofinansowania w ramach którego jest zawarta umowa';
COMMENT ON COLUMN APP_PBE.CONTRACTS.SIGN_DATE IS 'Dat podpisania umowy';
COMMENT ON COLUMN APP_PBE.CONTRACTS.EXPIRY_DATE IS 'Data wygaśnięcia umowy';

BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'CONTRACTS', 'ALL')); 
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'CONTRACTS', 'ALL'));   
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'CONTRACTS', 'SELECT'));      
END;
/ 
