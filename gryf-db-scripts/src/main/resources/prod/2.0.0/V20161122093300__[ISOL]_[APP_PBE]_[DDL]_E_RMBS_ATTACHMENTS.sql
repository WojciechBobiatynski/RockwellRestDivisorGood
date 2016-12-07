CREATE TABLE APP_PBE.E_RMBS_ATTACHMENTS
(
	ID NUMBER NOT NULL,    -- Id za³¹cznika. Klucz sztuczny. Sekwencja EAGLE.ERMBS_ATTACH_SEQ
	E_RMBS_ID NUMBER NOT NULL,    -- Id rozliczenia. Klucz obcy do APP_PBE.E_REIMBURSEMENTS
	ATTACH_TYPE VARCHAR2(10) NOT NULL,    -- Typ dokumentu. Klucz obcy do tabeli APP_PBE.ATTACHMENT_TYPES
	FILE_ID NUMBER, -- Id do rekordu z informacjami o zapisanym pliku.
	DOCUMENT_NUMBER VARCHAR2(200),    -- Numer dokumentu (Np. FV) lub opis
	ADDITIONAL_DESCRIPTION VARCHAR2(200),    -- Dodatkowy opis
	DELETED VARCHAR2(1) NOT NULL,    -- Flaga mówi¹ca czy dany za³¹cznik zosta³ usuniêty w ramach korekty
	VERSION NUMBER NOT NULL,    -- Standardowa kolumna wersji na potrzeby optymistycznego blokowania (Gryf)
	CREATED_USER VARCHAR2(100) NOT NULL,    -- U¿ytkownik tworzacy wiersz - kolumna audytowa
	CREATED_TIMESTAMP TIMESTAMP(6) NOT NULL,    -- Timestamp utworzenia wiersza - kolumna audytowa
	MODIFIED_USER VARCHAR2(100) NOT NULL,    -- Ostatni u¿ytkownik modyfikuj¹cy wiersz - kolumna audytowa
	MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL    -- Timestamp ostatniej modyfikacji wiersza - kolumna audytowa
)
;

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'E_RMBS_ATTACHMENTS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'E_RMBS_ATTACHMENTS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'E_RMBS_ATTACHMENTS', 'SELECT'));
END;
/

-- Indeksy
CREATE UNIQUE INDEX APP_PBE.E_RMBS_ATTACH_PK ON APP_PBE.E_RMBS_ATTACHMENTS (ID);

ALTER TABLE APP_PBE.E_RMBS_ATTACHMENTS
 ADD CONSTRAINT E_RMBS_ATTACH_PK
	PRIMARY KEY (ID);

ALTER TABLE APP_PBE.E_RMBS_ATTACHMENTS
 ADD CONSTRAINT E_RMBS_ATTACH_ATT_TYPE_FK
	FOREIGN KEY (ATTACH_TYPE) REFERENCES APP_PBE.ATTACHMENT_TYPES (CODE);

ALTER TABLE APP_PBE.E_RMBS_ATTACHMENTS
 ADD CONSTRAINT E_RMBS_ATTACH_R_RMBS_FK
	FOREIGN KEY (E_RMBS_ID) REFERENCES APP_PBE.E_REIMBURSEMENTS (ID);

COMMENT ON TABLE APP_PBE.E_RMBS_ATTACHMENTS IS '@Author(Adam Kmieciñski); @Project(Gryf-PBE); @Date(2016-11-22);@Purpose(Tabela przechowuj¹ca za³¹czniki dla danego rozliczenia);';

COMMENT ON COLUMN APP_PBE.E_RMBS_ATTACHMENTS.ID IS 'Id za³¹cznika. Klucz sztuczny. Sekwencja EAGLE.ERMBS_ATTACH_SEQ';
COMMENT ON COLUMN APP_PBE.E_RMBS_ATTACHMENTS.E_RMBS_ID IS 'Id rozliczenia. Klucz obcy do APP_PBE.E_REIMBURSEMENTS';
COMMENT ON COLUMN APP_PBE.E_RMBS_ATTACHMENTS.ATTACH_TYPE IS 'Typ dokumentu. Klucz obcy do tabeli APP_PBE.ATTACHMENT_TYPES';
COMMENT ON COLUMN APP_PBE.E_RMBS_ATTACHMENTS.FILE_ID IS 'Id do rekordu z informacjami o zapisanym pliku.';
COMMENT ON COLUMN APP_PBE.E_RMBS_ATTACHMENTS.DOCUMENT_NUMBER IS 'Numer dokumentu (Np. FV) lub opis';
COMMENT ON COLUMN APP_PBE.E_RMBS_ATTACHMENTS.ADDITIONAL_DESCRIPTION IS 'Dodatkowy opis';
COMMENT ON COLUMN APP_PBE.E_RMBS_ATTACHMENTS.DELETED IS 'Czy za³¹cznik usuniêty podczas korekty';
COMMENT ON COLUMN APP_PBE.E_RMBS_ATTACHMENTS.VERSION IS 'Standardowa kolumna wersji na potrzeby optymistycznego blokowania (Gryf)';
COMMENT ON COLUMN APP_PBE.E_RMBS_ATTACHMENTS.CREATED_USER IS 'U¿ytkownik tworzacy wiersz - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.E_RMBS_ATTACHMENTS.CREATED_TIMESTAMP IS 'Timestamp utworzenia wiersza - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.E_RMBS_ATTACHMENTS.MODIFIED_USER IS 'Ostatni u¿ytkownik modyfikuj¹cy wiersz - kolumna audytowa';
COMMENT ON COLUMN APP_PBE.E_RMBS_ATTACHMENTS.MODIFIED_TIMESTAMP IS 'Timestamp ostatniej modyfikacji wiersza - kolumna audytowa';

--------------------------------------------SEQUENCE--------------------------------------------
CREATE SEQUENCE EAGLE.ERMBS_ATTACH_SEQ
MINVALUE 1
MAXVALUE 9999999999999999999999999999
INCREMENT BY 1
START WITH 100
CACHE 20
NOORDER NOCYCLE;

-- uprawnienia
BEGIN
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE', 'ERMBS_ATTACH_SEQ', 'ALL'));
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'DEVELOPER', 'ERMBS_ATTACH_SEQ', 'SELECT'));
END;
/
--------------------------------------------------------------------------------------------------