CREATE TABLE APP_PBE.ATTACHMENT_FILES
(
	ID NUMBER NOT NULL,    -- Identyfikator obiektu. Sekwencja EAGLE.FILE_SEQ
	STATUS VARCHAR2(10) NOT NULL,    -- Status pliku (TO_DELETE, TEMP, SAVED, CORRECT, REMOVED)
	ORIGINAL_FILE_NAME VARCHAR(100) NOT NULL,
	FILE_LOCATION VARCHAR2(250) NOT NULL,
	VERSION NUMBER NOT NULL,
	CREATED_USER VARCHAR2(100) NOT NULL,
	CREATED_TIMESTAMP TIMESTAMP(6) NOT NULL,
	MODIFIED_USER VARCHAR2(100) NOT NULL,
	MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL
);

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'ATTACHMENT_FILES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'ATTACHMENT_FILES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'ATTACHMENT_FILES', 'SELECT'));
END;
/

/* Create Comments, Sequences and Triggers for Autonumber Columns */

COMMENT ON TABLE APP_PBE.ATTACHMENT_FILES IS '@Author(Adam Kmieciñski); @Project(Gryf-PBE); @Date(2016-12-07) ;@Purpose(Tabela przechowuj¹ca dane dotycz¹ce plików za³¹czników)';

COMMENT ON COLUMN APP_PBE.ATTACHMENT_FILES.ID IS 'Identyfikator obiektu. Sekwencja EAGLE.FILE_SEQ';
COMMENT ON COLUMN APP_PBE.ATTACHMENT_FILES.STATUS IS 'Status pliku (TO_DELETE, TEMP, SAVED, CORRECT, REMOVED)';
COMMENT ON COLUMN APP_PBE.ATTACHMENT_FILES.ORIGINAL_FILE_NAME IS 'oryginalna nazwa pliku';
COMMENT ON COLUMN APP_PBE.ATTACHMENT_FILES.FILE_LOCATION IS 'Pe³na œcie¿ka do pliku zapisanego na serwerze (razem z nazw¹ pliku)';
COMMENT ON COLUMN APP_PBE.ATTACHMENT_FILES.VERSION IS 'Standardowa kolumna wersji na potrzeby optymistycznego blokowania (Gryf)';
COMMENT ON COLUMN APP_PBE.ATTACHMENT_FILES.CREATED_USER IS 'U¿ytkownik tworzacy wiersz - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.ATTACHMENT_FILES.CREATED_TIMESTAMP IS 'Timestamp utworzenia wiersza - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.ATTACHMENT_FILES.MODIFIED_USER IS 'Ostatni u¿ytkownik modyfikuj¹cy wiersz - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.ATTACHMENT_FILES.MODIFIED_TIMESTAMP IS 'Timestamp ostatniej modyfikacji wiersza - kolumna audytowa';

/* Create Primary Keys, Indexes, Uniques, Checks, Triggers */

-- Indeksy
CREATE UNIQUE INDEX APP_PBE.ATTACHMENT_FILES_PK ON APP_PBE.ATTACHMENT_FILES (ID);

ALTER TABLE APP_PBE.ATTACHMENT_FILES 
 ADD CONSTRAINT ATTACHMENT_FILES_PK
	PRIMARY KEY (ID);

--------------------------------------------SEQUENCE--------------------------------------------
CREATE SEQUENCE EAGLE.FILE_SEQ
MINVALUE 1
MAXVALUE 9999999999999999999999999999
INCREMENT BY 1
START WITH 1
CACHE 20
NOORDER NOCYCLE;

-- uprawnienia
BEGIN
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE', 'FILE_SEQ', 'ALL'));
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'DEVELOPER', 'FILE_SEQ', 'SELECT'));
END;
/
--------------------------------------------------------------------------------------------------