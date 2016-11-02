CREATE TABLE APP_PBE.ACCOUNT_CONTRACT_PAIRS
(
	ID NUMBER NOT NULL,
	CONTRACT_ID NUMBER NOT NULL,
	ACCOUNT_PAYMENT VARCHAR2(26) NOT NULL,
	GRANT_PROGRAM_ID NUMBER,
	USED VARCHAR2(1) DEFAULT 0 NOT NULL,
	VERSION VARCHAR2(50) DEFAULT 1 NOT NULL,
	CREATED_USER VARCHAR2(100) NOT NULL,
	CREATED_TIMESTAMP TIMESTAMP(6) NOT NULL,
	MODIFIED_USER VARCHAR2(100) NOT NULL,
	MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL
)
;

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'ACCOUNT_CONTRACT_PAIRS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'ACCOUNT_CONTRACT_PAIRS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'ACCOUNT_CONTRACT_PAIRS', 'SELECT'));
END;
/

-- Indeksy
CREATE UNIQUE INDEX APP_PBE.ACC_CNT_PRS_PK ON APP_PBE.ACCOUNT_CONTRACT_PAIRS (ID);

-- Klucze
ALTER TABLE APP_PBE.ACCOUNT_CONTRACT_PAIRS
 ADD CONSTRAINT ACC_CNT_PRS_PK
	PRIMARY KEY (ID)
;

ALTER TABLE APP_PBE.ACCOUNT_CONTRACT_PAIRS
 ADD CONSTRAINT ACC_CNT_PRS_GRT_PRG_FK
	FOREIGN KEY (GRANT_PROGRAM_ID) REFERENCES APP_PBE.GRANT_PROGRAMS (ID)
;

-- Komentarze
COMMENT ON TABLE APP_PBE.ACCOUNT_CONTRACT_PAIRS IS '@Author(Adam Kmieci�ski); @Project(GryfBonEl); @Date(2016-11-02);@Purpose(Tabel przechowuj�ca wygenerowane pary subkonto-kontrakt);';

COMMENT ON COLUMN APP_PBE.ACCOUNT_CONTRACT_PAIRS.ID IS 'Identyfikator tabeli. Klucz sztuczny. Sekwencja EAGLE.PK_SEQ';
COMMENT ON COLUMN APP_PBE.ACCOUNT_CONTRACT_PAIRS.CONTRACT_ID IS 'Id umowy. Generowane na podstawie sekwencji EAGLE.CNT_SEQ';
COMMENT ON COLUMN APP_PBE.ACCOUNT_CONTRACT_PAIRS.ACCOUNT_PAYMENT IS 'Wygenerowany numer subkonta na podstawie sekwencji EAGLE.IND_SEQ';
COMMENT ON COLUMN APP_PBE.ACCOUNT_CONTRACT_PAIRS.GRANT_PROGRAM_ID IS 'Id programu dofinansowania';
COMMENT ON COLUMN APP_PBE.ACCOUNT_CONTRACT_PAIRS.USED IS 'Czy para zosta�a ju� wykorzystana';
COMMENT ON COLUMN APP_PBE.ACCOUNT_CONTRACT_PAIRS.VERSION IS 'Wersja. Kolumna techniczna u�ywana do mechanizmu Optimistic Lock';
COMMENT ON COLUMN APP_PBE.ACCOUNT_CONTRACT_PAIRS.CREATED_USER IS 'Login u�ytkownika, kt�ry utworzy� rekord';
COMMENT ON COLUMN APP_PBE.ACCOUNT_CONTRACT_PAIRS.CREATED_TIMESTAMP IS 'Data utworzenia rekordu';
COMMENT ON COLUMN APP_PBE.ACCOUNT_CONTRACT_PAIRS.MODIFIED_USER IS 'Login u�ytkownika, kt�ry ostatnio modyfikowa� rekord';
COMMENT ON COLUMN APP_PBE.ACCOUNT_CONTRACT_PAIRS.MODIFIED_TIMESTAMP IS 'Data ostatniej modyfikacji rekordu';