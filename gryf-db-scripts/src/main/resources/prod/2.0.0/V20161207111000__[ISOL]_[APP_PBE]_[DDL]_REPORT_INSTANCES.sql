CREATE TABLE APP_PBE.REPORT_INSTANCES
(
	ID NUMBER NOT NULL,    -- Identyfikator wydruku. Klucz g³ówny
	TEMPLATE_NAME VARCHAR (100) NOT NULL,   -- Nazwa szablonu wykonywanego raportu
	CREATED_USER VARCHAR(100) NOT NULL,   -- U¿ytkownik wykonuj¹cy raport
	CREATED_TIMESTAMP TIMESTAMP(6) NOT NULL,    -- Data wykonania raportu
	PARAMETERS CLOB NOT NULL,   -- Lista parametrów raportu
	PATH VARCHAR(200) NOT NULL,   -- Link do pliku wynikowego raportu
	SOURCE_TYPE VARCHAR(30) NOT NULL,    -- Rodzaj Ÿród³a dla raportu - np. zamówienie, rozliczenie
	SOURCE_ID NUMBER NOT NULL    -- ID obiektu powi¹zanego, np. id zamówienia, id rozliczenia
);

COMMENT ON TABLE APP_PBE.REPORT_INSTANCES IS '@Author(Andrzej Dziobek); @Project(Gryf-PBE); @Date(2016-12-07);@Purpose(Tabela przechowuj¹ca dane wydruków);';
COMMENT ON COLUMN APP_PBE.REPORT_INSTANCES.ID IS 'Identyfikator wydruku. Klucz g³ówny. Sekwencja PBE_REPORT_INSTANCES_SEQ';
COMMENT ON COLUMN APP_PBE.REPORT_INSTANCES.TEMPLATE_NAME IS 'Nazwa szablonu wykonywanego raportu';
COMMENT ON COLUMN APP_PBE.REPORT_INSTANCES.CREATED_USER IS 'U¿ytkownik wykonuj¹cy raport';
COMMENT ON COLUMN APP_PBE.REPORT_INSTANCES.CREATED_TIMESTAMP  IS 'Data wykonania raportu';
COMMENT ON COLUMN APP_PBE.REPORT_INSTANCES.PARAMETERS IS 'Data wykonania raportu';
COMMENT ON COLUMN APP_PBE.REPORT_INSTANCES.PATH IS 'Link do pliku wynikowego raportu';
COMMENT ON COLUMN APP_PBE.REPORT_INSTANCES.SOURCE_TYPE  IS 'Rodzaj Ÿród³a dla raportu';
COMMENT ON COLUMN APP_PBE.REPORT_INSTANCES.SOURCE_ID  IS 'ID obiektu powi¹zanego';

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'REPORT_INSTANCES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'REPORT_INSTANCES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'REPORT_INSTANCES', 'SELECT'));
END;
/
-- Indeksy
CREATE UNIQUE INDEX APP_PBE.PK_REPORT_INSTANCES_ID ON APP_PBE.REPORT_INSTANCES (ID);

ALTER TABLE APP_PBE.REPORT_INSTANCES
 ADD CONSTRAINT PK_REPORT_INSTANCES_ID
	PRIMARY KEY (ID);

