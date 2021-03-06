CREATE TABLE APP_PBE.INDIVIDUAL_USERS
(
	INU_ID NUMBER NOT NULL,
	IND_ID NUMBER NOT NULL,
	VERIFICATION_CODE VARCHAR2(60) NOT NULL,
	IS_ACTIVE VARCHAR2(1) NOT NULL,
	LAST_LOGIN_SUCCESS_DATE DATE,
	LAST_LOGIN_FAILURE_DATE DATE,
	LOGIN_FAILURE_ATTEMPTS NUMBER(1) DEFAULT 0,
	LAST_RESET_FAILURE_DATE DATE,
	RESET_FAILURE_ATTEMPTS NUMBER(1) DEFAULT 0,
	VERSION VARCHAR2(50) DEFAULT 1 NOT NULL,
	CREATED_USER VARCHAR2(100) NOT NULL,
	CREATED_TIMESTAMP TIMESTAMP(6) NOT NULL,
	MODIFIED_USER VARCHAR(100),
	MODIFIED_TIMESTAMP TIMESTAMP(6)
)
;

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'INDIVIDUAL_USERS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'INDIVIDUAL_USERS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'INDIVIDUAL_USERS', 'SELECT'));
END;
/

-- Indeksy
CREATE UNIQUE INDEX APP_PBE.INU_ID_PK ON APP_PBE.INDIVIDUAL_USERS (INU_ID);

-- Klucze
ALTER TABLE APP_PBE.INDIVIDUAL_USERS 
 ADD CONSTRAINT INU_ID_PK
	PRIMARY KEY (INU_ID)
;

ALTER TABLE APP_PBE.INDIVIDUAL_USERS 
 ADD CONSTRAINT IND_ID_INU_FK
	FOREIGN KEY (IND_ID) REFERENCES APP_PBE.INDIVIDUALS (ID)
;

-- Komentarze
COMMENT ON TABLE APP_PBE.INDIVIDUAL_USERS IS '@Author(Adam Kmieciński); @Project(Gryf-PBE); @Date(2016-10-18) ;@Purpose(Tabela przechowująca użytkowników osób fizycznych);';

COMMENT ON COLUMN APP_PBE.INDIVIDUAL_USERS.INU_ID IS 'Unikalne ID użytkownika osoby fizycznej. Klucz główny. Sekwencja IND_USR_SEQ';
COMMENT ON COLUMN APP_PBE.INDIVIDUAL_USERS.IND_ID IS 'ID osoby fizycznej';
COMMENT ON COLUMN APP_PBE.INDIVIDUAL_USERS.VERIFICATION_CODE IS 'Kod weryfikacyjny na potrzeby logowania';
COMMENT ON COLUMN APP_PBE.INDIVIDUAL_USERS.IS_ACTIVE IS 'Czy użytkownik jest aktywny';
COMMENT ON COLUMN APP_PBE.INDIVIDUAL_USERS.LAST_LOGIN_SUCCESS_DATE IS 'Data ostatniego logowania zakończonego sukcesem';
COMMENT ON COLUMN APP_PBE.INDIVIDUAL_USERS.LAST_LOGIN_FAILURE_DATE IS 'Data ostatniego niepowodzenia logowania spowodowana błędnymi danymi';
COMMENT ON COLUMN APP_PBE.INDIVIDUAL_USERS.LOGIN_FAILURE_ATTEMPTS IS 'Ilość kolejnych niepowodzeń logowania spowodowanych błędnymi danymi';
COMMENT ON COLUMN APP_PBE.INDIVIDUAL_USERS.LAST_RESET_FAILURE_DATE IS 'Data ostatniego niepowodzenia otrzymania nowego kodu weryfikacyjnego spowodowana błędnymi danymi';
COMMENT ON COLUMN APP_PBE.INDIVIDUAL_USERS.RESET_FAILURE_ATTEMPTS IS 'Ilość kolejnych niepowodzeń otrzymania nowego kodu weryfikacyjnego spowodowanych błędnymi danymi';
COMMENT ON COLUMN APP_PBE.INDIVIDUAL_USERS.VERSION IS 'Wersja. Kolumna techniczna używana do mechanizmu Optimistic Lock';
COMMENT ON COLUMN APP_PBE.INDIVIDUAL_USERS.CREATED_USER IS 'Login użytkownika, który utworzył rekord';
COMMENT ON COLUMN APP_PBE.INDIVIDUAL_USERS.CREATED_TIMESTAMP IS 'Data utworzenia rekordu';
COMMENT ON COLUMN APP_PBE.INDIVIDUAL_USERS.MODIFIED_USER IS 'Login użytkownika, który ostatnio modyfikował rekord';
COMMENT ON COLUMN APP_PBE.INDIVIDUAL_USERS.MODIFIED_TIMESTAMP IS 'Data ostatniej modyfikacji rekordu';
