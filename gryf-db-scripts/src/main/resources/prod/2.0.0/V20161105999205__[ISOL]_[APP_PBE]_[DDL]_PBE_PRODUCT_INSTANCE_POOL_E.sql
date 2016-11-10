

CREATE TABLE APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E
(
	ID NUMBER NOT NULL,
	PRODUCT_INSTANCE_POOL_ID NUMBER(10) NOT NULL,
	TYPE_ID VARCHAR2(10 BYTE) NOT NULL,
	SOURCE_ID VARCHAR2(50 BYTE),
	VERSION NUMBER NOT NULL,
	CREATED_USER VARCHAR2(100 BYTE) NOT NULL,
	CREATED_TIMESTAMP TIMESTAMP(6) NOT NULL,
	MODIFIED_USER VARCHAR2(100 BYTE) NOT NULL,
	MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL
)
;

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'PBE_PRODUCT_INSTANCE_POOL_E', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'PBE_PRODUCT_INSTANCE_POOL_E', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'PBE_PRODUCT_INSTANCE_POOL_E', 'SELECT'));
END;
/

-- Klucze
CREATE UNIQUE INDEX APP_PBE.PBE_PRD_IN_POL_EVN_PK ON APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E (ID);
ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E ADD CONSTRAINT PBE_PRD_IN_POL_EVN_PK PRIMARY KEY (ID);

-- Klucze obce
ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E ADD CONSTRAINT PBE_PRD_INS_POL_EVE_FK FOREIGN KEY (PRODUCT_INSTANCE_POOL_ID) REFERENCES APP_PBE.PBE_PRODUCT_INSTANCE_POOLS (ID);
ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E ADD CONSTRAINT PBE_PRD_INS_POL_EVE_TYP_FK FOREIGN KEY (TYPE_ID) REFERENCES APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E_T (ID);

-- Komentarze
COMMENT ON TABLE APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E IS '@Author(Adam Kmieci�ski); @Project(GryfBonEl); @Date(2016-10-11);@Purpose(Tabela przechowuj�ca dane instancji produkt�w);';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E.ID IS 'ID zdarzenia instancji puli produktu. Klucz g��wny';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E.PRODUCT_INSTANCE_POOL_ID IS 'ID instancji puli produkt�w';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E.TYPE_ID IS 'Kod rodzaju zdarzenia dla instancji. Warto�� s�ownikowa';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E.SOURCE_ID IS 'ID obiektu �r�d�owego dla zdarzenia - powi�zanie z tabel� �r�d�ow� wynika z rodzaju zdarzenia (np wydanie b�dzie zwi�zane z zam�wieniem itp.)';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E.VERSION IS 'Wersja. Kolumna techniczna u�ywana do mechanizmu Optimistic Lock';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E.CREATED_USER IS 'Login u�ytkownika, kt�ry utworzy� rekord';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E.CREATED_TIMESTAMP IS 'Data utworzenia rekordu';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E.MODIFIED_USER IS 'Login u�ytkownika, kt�ry ostatnio modyfikowa� rekord';
COMMENT ON COLUMN APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E.MODIFIED_TIMESTAMP IS 'Data ostatniej modyfikacji rekordu';