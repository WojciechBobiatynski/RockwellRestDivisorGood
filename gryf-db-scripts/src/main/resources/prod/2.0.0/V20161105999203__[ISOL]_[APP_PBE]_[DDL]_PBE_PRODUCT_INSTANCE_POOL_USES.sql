

CREATE TABLE APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES
(
	ID NUMBER NOT NULL,
	TRAINING_INSTANCE_ID NUMBER NOT NULL,
	PRODUCT_INSTANCE_POOL_ID NUMBER NOT NULL,
	ASSIGNED_NUM NUMBER(10) NOT NULL,
	VERSION NUMBER NOT NULL,
	CREATED_USER VARCHAR2(30 BYTE) NOT NULL,
	CREATED_TIMESTAMP TIMESTAMP(6) NOT NULL,
	MODIFIED_USER VARCHAR2(30 BYTE) NOT NULL,
	MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL
)
;

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'PBE_PRODUCT_INSTANCE_POOL_USES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'PBE_PRODUCT_INSTANCE_POOL_USES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'PBE_PRODUCT_INSTANCE_POOL_USES', 'SELECT'));
END;
/

-- Klucze
CREATE UNIQUE INDEX APP_PBE.PBE_PRD_INS_POL_USE_PK ON APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES (ID);
ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES ADD CONSTRAINT PBE_PRD_INS_POL_USE_PK PRIMARY KEY (ID);

-- Klucze obce
ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES ADD CONSTRAINT PBE_PRD_INS_POL_USE_TRA_FK FOREIGN KEY (TRAINING_INSTANCE_ID) REFERENCES APP_PBE.TI_TRAINING_INSTANCES (ID);
ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES ADD CONSTRAINT PBE_PRD_INS_POL_POL_FK FOREIGN KEY (PRODUCT_INSTANCE_POOL_ID) REFERENCES APP_PBE.PBE_PRODUCT_INSTANCE_POOLS (ID);

-- Komentarze
COMMENT ON TABLE APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES IS '@Author(Tomasz Bilski); @Project(GryfBonEl); @Date(2016-11-05);@Purpose(Tabel przechwouj�ca informacjie o wykorzystaniu puli bon�w w danym szkoleniu (instancji szkolenia));';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES.ID IS 'ID instancji wykorzystania puli bon�w. Klucz g��wny';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES.TRAINING_INSTANCE_ID IS 'Instancja szkolenia w ramach kt�rej wykorzystana jest pula bon�w';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES.PRODUCT_INSTANCE_POOL_ID IS 'Wykorzystana pula bon�w';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES.ASSIGNED_NUM IS 'Ilo�� wykorzystanych bon�w';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES.VERSION IS 'Wersja. Kolumna techniczna u�ywana do mechanizmu Optimistic Lock';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES.CREATED_USER IS 'Login u�ytkownika, kt�ry utworzy� rekord';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES.CREATED_TIMESTAMP IS 'Data utworzenia rekordu';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES.MODIFIED_USER IS 'Login u�ytkownika, kt�ry ostatnio modyfikowa� rekord';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES.MODIFIED_TIMESTAMP IS 'Data ostatniej modyfikacji rekordu';
