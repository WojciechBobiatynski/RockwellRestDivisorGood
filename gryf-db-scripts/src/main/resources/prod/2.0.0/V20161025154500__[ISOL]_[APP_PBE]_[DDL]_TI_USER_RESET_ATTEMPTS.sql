CREATE TABLE APP_PBE.TI_USER_RESET_ATTEMPTS
(
	TUR_ID VARCHAR2(60) NOT NULL,
	TIU_ID NUMBER NOT NULL,
	EXPIRY_DATE DATE NOT NULL,
	USED VARCHAR2(1) NOT NULL,
	VERSION NUMBER NOT NULL,
	CREATED_USER VARCHAR2(100) NOT NULL,
	CREATED_TIMESTAMP TIMESTAMP(6) NOT NULL,
	MODIFIED_USER VARCHAR2(100) NOT NULL,
	MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL
)
;

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'TI_USER_RESET_ATTEMPTS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'TI_USER_RESET_ATTEMPTS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'TI_USER_RESET_ATTEMPTS', 'SELECT'));
END;
/

-- Indeksy
CREATE UNIQUE INDEX APP_PBE.TUR_ID_PK ON APP_PBE.TI_USER_RESET_ATTEMPTS (TUR_ID);

-- Klucze
ALTER TABLE APP_PBE.TI_USER_RESET_ATTEMPTS 
 ADD CONSTRAINT TUR_ID_PK
	PRIMARY KEY (TUR_ID)
;

ALTER TABLE APP_PBE.TI_USER_RESET_ATTEMPTS 
 ADD CONSTRAINT TI_USER_RESET_ATT_TIU_ID_FK
	FOREIGN KEY (TIU_ID) REFERENCES APP_PBE.TRAINING_INSTITUTION_USERS (ID)
;

-- Komentarze
COMMENT ON TABLE APP_PBE.TI_USER_RESET_ATTEMPTS IS '@Author(Adam Kmieci?ski); @Project(Gryf-PBE); @Date(2016-10-25) ;@Purpose(Tabel reprezentuj?ca zdarzenia ??dania zresetowania has?a dla instytucji szkoleniowej);';

COMMENT ON COLUMN APP_PBE.TI_USER_RESET_ATTEMPTS.TUR_ID IS 'Unikalne ID generowane przez UUID. Cz??? linku do resetowania has?a';
COMMENT ON COLUMN APP_PBE.TI_USER_RESET_ATTEMPTS.TIU_ID IS 'ID instytucji szkoleniowej';
COMMENT ON COLUMN APP_PBE.TI_USER_RESET_ATTEMPTS.EXPIRY_DATE IS 'Data wyga?ni?cia wygenerowanego linku na podstawie TUR_ID';
COMMENT ON COLUMN APP_PBE.TI_USER_RESET_ATTEMPTS.USED IS 'Czy link zosta? wykorzystany';
COMMENT ON COLUMN APP_PBE.TI_USER_RESET_ATTEMPTS.VERSION IS 'Wersja. Kolumna techniczna u?ywana do mechanizmu Optimistic Lock';
COMMENT ON COLUMN APP_PBE.TI_USER_RESET_ATTEMPTS.CREATED_USER IS 'Login u?ytkownika, kt?ry utworzy? rekord';
COMMENT ON COLUMN APP_PBE.TI_USER_RESET_ATTEMPTS.CREATED_TIMESTAMP IS 'Data utworzenia rekordu';
COMMENT ON COLUMN APP_PBE.TI_USER_RESET_ATTEMPTS.MODIFIED_USER IS 'Login u?ytkownika, kt?ry ostatnio modyfikowa? rekord';
COMMENT ON COLUMN APP_PBE.TI_USER_RESET_ATTEMPTS.MODIFIED_TIMESTAMP IS 'Data ostatniej modyfikacji rekordu';