CREATE TABLE APP_PBE.ATTACHMENT_TYPES
(
	CODE VARCHAR2(10) NOT NULL,    -- Kod za³¹cznika. Klucz g³ówny
	NAME VARCHAR2(200) NOT NULL,    -- Nazwa/opis za³¹cznika
	ORDINAL NUMBER,    -- Liczba porz¹dkowa
	MAX_BYTES_PER_FILE NUMBER,    -- Maksymalna liczba bajtów dla danego za³¹cznika
	MAX_FILES_PER_TYPE NUMBER    -- Maksymalna liczba za³¹czników dla danego typu za³¹cznika
);

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'ATTACHMENT_TYPES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'ATTACHMENT_TYPES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'ATTACHMENT_TYPES', 'SELECT'));
END;
/

-- Indeksy
CREATE UNIQUE INDEX APP_PBE.ATTACH_TYPE_PK ON APP_PBE.ATTACHMENT_TYPES (CODE);

ALTER TABLE APP_PBE.ATTACHMENT_TYPES
 ADD CONSTRAINT ATTACH_TYPE_PK
	PRIMARY KEY (CODE);

COMMENT ON TABLE APP_PBE.ATTACHMENT_TYPES IS '@Author(Adam Kmieciñski); @Project(Gryf-PBE); @Date(2016-11-22) ;@Purpose(Tabela s³ownikowa dla typów za³¹czników);';

COMMENT ON COLUMN APP_PBE.ATTACHMENT_TYPES.CODE IS 'Kod za³¹cznika. Klucz g³ówny';
COMMENT ON COLUMN APP_PBE.ATTACHMENT_TYPES.NAME IS 'Nazwa/opis za³¹cznika';
COMMENT ON COLUMN APP_PBE.ATTACHMENT_TYPES.ORDINAL IS 'Liczba porz¹dkowa';
COMMENT ON COLUMN APP_PBE.ATTACHMENT_TYPES.MAX_BYTES_PER_FILE IS 'Maksymalna liczba bajtów dla danego za³¹cznika';
COMMENT ON COLUMN APP_PBE.ATTACHMENT_TYPES.MAX_FILES_PER_TYPE IS 'Maksymalna liczba za³¹czników dla danego typu za³¹cznika';