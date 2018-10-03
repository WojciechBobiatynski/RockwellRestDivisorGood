--------------------------------------------SEQUENCE--------------------------------------------
CREATE SEQUENCE EAGLE.JOB_TYPE_SEQ
MINVALUE 1
MAXVALUE 9999999999999999999999999999
INCREMENT BY 1
START WITH 100
CACHE 20
NOORDER NOCYCLE;

-- uprawnienia
BEGIN
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE', 'JOB_TYPE_SEQ', 'ALL'));
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'DEVELOPER', 'JOB_TYPE_SEQ', 'SELECT'));
END;
/
--------------------------------------------------------------------------------------------------

--------------------------------------------TABELA IMPORT TYPE --------------------------------------------
CREATE TABLE APP_PBE.JOB_TYPE
(
  ID NUMBER NOT NULL
, NAME VARCHAR2(500 BYTE) NOT NULL
, GRANT_PROGRAM_ID NUMBER NULL
, LABEL VARCHAR2(500 BYTE) NOT NULL
, JOB_NAME VARCHAR2(500 BYTE) NOT NULL
, SERVICE_NAME VARCHAR2(500 BYTE) NULL
);

CREATE UNIQUE INDEX APP_PBE.JOB_TYPE_PK ON APP_PBE.JOB_TYPE (ID ASC);

ALTER TABLE APP_PBE.JOB_TYPE
ADD CONSTRAINT JOB_TYPE_PK PRIMARY KEY
(
  ID
)
USING INDEX APP_PBE.JOB_TYPE_PK
ENABLE;

ALTER TABLE APP_PBE.JOB_TYPE
ADD CONSTRAINT JOB_TYPE_GRANT_PROGRAM_FK FOREIGN KEY
(
  GRANT_PROGRAM_ID
)
REFERENCES APP_PBE.GRANT_PROGRAMS
(
  ID
)
ENABLE;

ALTER TABLE APP_PBE.JOB_TYPE
ADD CONSTRAINT JOB_TYPE_NAME_UK UNIQUE
(
  NAME
)
ENABLE;

COMMENT ON TABLE APP_PBE.JOB_TYPE IS 'Tabela przechowuje konfiguracje typów zadań importu per Program/Projekt';

COMMENT ON COLUMN APP_PBE.JOB_TYPE.ID IS 'Klucz głowny generowany z sekwencji JOB_TYPE_seq';

COMMENT ON COLUMN APP_PBE.JOB_TYPE.GRANT_PROGRAM_ID IS 'Klucz obcy do programu ';

COMMENT ON COLUMN APP_PBE.JOB_TYPE.JOB_NAME IS 'Nazwa automatycznego zadania np: joba realizowaneog przez implementacje schedulera po stronie backendu';

COMMENT ON COLUMN APP_PBE.JOB_TYPE.SERVICE_NAME IS 'Nazwa usługi implementujacej proces importu ';

--------------------------------------------------------------------------------------------------

-- uprawnienia
BEGIN
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'JOB_TYPE', 'ALL'));
END;
/