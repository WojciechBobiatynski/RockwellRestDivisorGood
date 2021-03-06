CREATE TABLE APP_PBE.TI_TRAINING_INSTANCES
(
	ID NUMBER NOT NULL,
	TRAINING_ID NUMBER NOT NULL,
	INDIVIDUAL_ID NUMBER NOT NULL,
	GRANT_PROGRAM_ID NUMBER NOT NULL,
	STATUS_ID VARCHAR2(10 BYTE) NOT NULL,
	ASSIGNED_NUM NUMBER(10) NOT NULL,
	REGISTER_DATE DATE NOT NULL,
	REIMBURSMENT_PIN VARCHAR2(50 BYTE),
	OPINION_DONE VARCHAR2(1 BYTE) default 'N' NOT NULL,
	VERSION NUMBER NOT NULL,
	CREATED_USER VARCHAR2(30 BYTE) NOT NULL,
	CREATED_TIMESTAMP TIMESTAMP(6) NOT NULL,
	MODIFIED_USER VARCHAR2(30 BYTE) NOT NULL,
	MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL
)
;

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'TI_TRAINING_INSTANCES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'TI_TRAINING_INSTANCES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'TI_TRAINING_INSTANCES', 'SELECT'));
END;
/

-- Klucze
CREATE UNIQUE INDEX APP_PBE.TI_TRA_INS_PK ON APP_PBE.TI_TRAINING_INSTANCES (ID);
ALTER TABLE APP_PBE.TI_TRAINING_INSTANCES ADD CONSTRAINT TI_TRA_INS_PK PRIMARY KEY (ID);

-- Klucze obce
ALTER TABLE APP_PBE.TI_TRAINING_INSTANCES ADD CONSTRAINT PBE_TRA_INS_TRA_FK FOREIGN KEY (TRAINING_ID) REFERENCES APP_PBE.TI_TRAININGS (ID);
ALTER TABLE APP_PBE.TI_TRAINING_INSTANCES ADD CONSTRAINT PBE_TRA_INS_IND_FK FOREIGN KEY (INDIVIDUAL_ID) REFERENCES APP_PBE.INDIVIDUALS (ID);
ALTER TABLE APP_PBE.TI_TRAINING_INSTANCES ADD CONSTRAINT PBE_TRA_INS_GRA_PRO_FK FOREIGN KEY (GRANT_PROGRAM_ID) REFERENCES APP_PBE.GRANT_PROGRAMS (ID);


-- Komentarze
COMMENT ON TABLE APP_PBE.TI_TRAINING_INSTANCES IS '@Author(Tomasz Bilski); @Project(GryfBonEl); @Date(2016-11-05);@Purpose(Tabel warto?ci dla instancji szkole? - szkole? w kontek?cie u?ytkownika);';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES.ID IS 'ID instancji szkolenia. Klucz g??wny';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES.TRAINING_ID IS 'ID szkolenia';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES.INDIVIDUAL_ID IS 'ID u?ytkownika ucz?szczaj?cego na szkolenie';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES.GRANT_PROGRAM_ID IS 'ID programu dofinansowania';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES.STATUS_ID IS 'Identyfikator statusu';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES.ASSIGNED_NUM IS 'Ilo?? wykorzystanych bon?w na dane szkolenie';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES.REGISTER_DATE IS 'Data zapisania na szkolenie';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES.REGISTER_DATE IS 'Data zarejestrowania na szkolenie';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES.REIMBURSMENT_PIN IS 'PIN do rozliczenia szkolenia';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES.OPINION_DONE IS 'Czy ocena za szkolenie zosta?a dokonana';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES.VERSION IS 'Wersja. Kolumna techniczna u?ywana do mechanizmu Optimistic Lock';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES.CREATED_USER IS 'Login u?ytkownika, kt?ry utworzy? rekord';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES.CREATED_TIMESTAMP IS 'Data utworzenia rekordu';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES.MODIFIED_USER IS 'Login u?ytkownika, kt?ry ostatnio modyfikowa? rekord';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES.MODIFIED_TIMESTAMP IS 'Data ostatniej modyfikacji rekordu';
