CREATE TABLE APP_PBE.TI_TRAININGS
(
	ID NUMBER(8,2) NOT NULL,
	EXTERNAL_ID VARCHAR2(10 BYTE),
	TRAINING_INSTITUTION_ID NUMBER(8,2) NOT NULL,
	NAME VARCHAR2(200) NOT NULL,
	PRICE NUMBER(10,3) NOT NULL,
	START_DATE DATE NOT NULL,
	END_DATE DATE NOT NULL,
	PLACE VARCHAR2(200) NOT NULL,
	HOURS_NUMBER NUMBER(8,2),
	HOUR_PRICE NUMBER(10,3),
	TRAINING_CATEGORY_ID VARCHAR2(5) NOT NULL,
	REIMBURSMENT_CONDITIONS VARCHAR2(1000),
	ACTIVE VARCHAR2(1 BYTE) DEFAULT 'Y' NOT NULL,
	DEACTIVATE_USER VARCHAR2(100),
	DEACTIVATE_DATE TIMESTAMP(6),
	DEACTIVATE_JOB_ID NUMBER,
	VERSION NUMBER NOT NULL,
	CREATED_USER VARCHAR2(100) NOT NULL,
	CREATED_TIMESTAMP TIMESTAMP(6) NOT NULL,
	MODIFIED_USER VARCHAR2(100) NOT NULL,
	MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL
);

-- constrainty i indeksy
CREATE UNIQUE INDEX APP_PBE.TI_TRA_PK  ON APP_PBE.TI_TRAININGS (ID);
ALTER TABLE APP_PBE.TI_TRAININGS ADD CONSTRAINT TI_TRA_PK PRIMARY KEY (ID);
	
ALTER TABLE APP_PBE.TI_TRAININGS ADD CONSTRAINT TRA_CAT_FK FOREIGN KEY (TRAINING_CATEGORY_ID) REFERENCES APP_PBE.TI_TRAINING_CATEGORIES (ID);
ALTER TABLE APP_PBE.TI_TRAININGS ADD CONSTRAINT TRIN_FK FOREIGN KEY (TRAINING_INSTITUTION_ID) REFERENCES APP_PBE.TRAINING_INSTITUTIONS (ID);
ALTER TABLE APP_PBE.TI_TRAININGS ADD CONSTRAINT TRA_INSTI_ASYN_JOB_FK FOREIGN KEY (DEACTIVATE_JOB_ID) REFERENCES APP_PBE.ASYNCH_JOBS (ID);

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'TI_TRAININGS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'TI_TRAININGS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'TI_TRAININGS', 'SELECT'));
END;
/

-- komentarze
COMMENT ON TABLE APP_PBE.TI_TRAININGS IS '@Author(Krzysztof Antczak); @Project(Gryf-PBE); @Date(2016-10-25);@Purpose(Tabela przechowuj�ca dane dotycz�ce szkole� organizowanych przez IS);';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.ID IS 'Identyfikator szkolenia';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.EXTERNAL_ID IS 'Identyfikator zewn�trzny z BUR';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.TRAINING_INSTITUTION_ID IS 'Identyfikator instytucji szkoleniowej';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.NAME IS 'Nazwa szkolenia';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.PRICE IS 'Cena szkolenia';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.START_DATE IS 'Data rozpocz�cia szkolenia';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.END_DATE IS 'Data zako�czenia szkolenia';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.PLACE IS 'Miejsce szkolenia';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.HOURS_NUMBER IS 'Liczba godzin szkolenia';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.HOUR_PRICE IS 'Cena za godzin� szkolenia';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.TRAINING_CATEGORY_ID IS 'Kod kategorii szkolenia';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.REIMBURSMENT_CONDITIONS IS 'Warunki rozliczenia szkolenia';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.ACTIVE IS 'Flaga czy dany rekord jest aktywny';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.DEACTIVATE_DATE IS 'Data deaktywacji';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.DEACTIVATE_JOB_ID IS 'Identyfikator zadania kt�re deaktywowa�o rekord';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.VERSION IS 'Wersja. Kolumna techniczna u�ywana do mechanizmu Optimistic Lock';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.CREATED_USER IS 'Nazwa u�ytkownika, kt�ry utworzy� rekord';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.CREATED_TIMESTAMP IS 'Data i czas momentu utworzenia rekordu';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.MODIFIED_USER IS 'Nazwa u�ytkownika, kt�ry ostatnio modyfikowa� rekord';
COMMENT ON COLUMN APP_PBE.TI_TRAININGS.MODIFIED_TIMESTAMP IS 'Data i czas ostatniej modyfikacji rekordu';
