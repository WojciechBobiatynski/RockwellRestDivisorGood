CREATE TABLE APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS
(
	PIE_ID NUMBER NOT NULL,
	PRD_ID VARCHAR2(20) NOT NULL,
	PRI_NUMBER NUMBER(10) NOT NULL,
	PET_CODE VARCHAR2(50),
	SOURCE_ID VARCHAR2(50),
	VERSION NUMBER NOT NULL,
	CREATED_USER VARCHAR2(100) NOT NULL,
	CREATED_TIMESTAMP TIMESTAMP(6) NOT NULL,
	MODIFIED_USER VARCHAR2(100),
	MODIFIED_TIMESTAMP TIMESTAMP(6)
)
;

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'PBE_PRODUCT_INSTANCE_EVENTS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'PBE_PRODUCT_INSTANCE_EVENTS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'PBE_PRODUCT_INSTANCE_EVENTS', 'SELECT'));
END;
/

-- Indeksy
CREATE UNIQUE INDEX APP_PBE.PBE_PRD_IN_EVN_PK ON APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS (PIE_ID);

-- Klucze
ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS 
 ADD CONSTRAINT PBE_PRD_IN_EVN_PK
	PRIMARY KEY (PIE_ID)
;

ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS 
 ADD CONSTRAINT PBE_PRD_IN_FK
	FOREIGN KEY (PRI_NUMBER,PRD_ID) REFERENCES APP_PBE.PBE_PRODUCT_INSTANCES (PRI_NUMBER,PRD_ID)
;

ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS 
 ADD CONSTRAINT PBE_PRD_INST_EVN_TYP_DIC_FK
	FOREIGN KEY (PET_CODE) REFERENCES APP_PBE.PBE_PRD_INST_EVN_TYPE_DICS (PET_CODE)
;

-- Komentarze
COMMENT ON TABLE APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS IS '@Author(Adam Kmieci�ski); @Project(GryfBonEl); @Date(2016-10-11);@Purpose(Tabela przechowuj�ca dane instancji produkt�w);';

COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS.PIE_ID IS 'ID zdarzenia instancji produktu. Klucz g��wny';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS.PRD_ID IS 'ID produktu';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS.PRI_NUMBER IS 'Numer instancji produktu';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS.PET_CODE IS 'Kod rodzaju zdarzenia dla instancji. Warto�� s�ownikowa';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS.SOURCE_ID IS 'ID obiektu �r�d�owego dla zdarzenia - powi�zanie z tabel� �r�d�ow� wynika z rodzaju zdarzenia (np wydanie b�dzie zwi�zane z zam�wieniem itp.)';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS.VERSION IS 'Wersja. Kolumna techniczna u�ywana do mechanizmu Optimistic Lock';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS.CREATED_USER IS 'Login u�ytkownika, kt�ry utworzy� rekord';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS.CREATED_TIMESTAMP IS 'Data utworzenia rekordu';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS.MODIFIED_USER IS 'Login u�ytkownika, kt�ry ostatnio modyfikowa� rekord';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS.MODIFIED_TIMESTAMP IS 'Data ostatniej modyfikacji rekordu';