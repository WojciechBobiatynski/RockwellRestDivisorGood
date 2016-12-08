ALTER TABLE APP_PBE.ORDERS ADD (CONTRACT_ID NUMBER);
ALTER TABLE APP_PBE.ORDERS ADD CONSTRAINT ORD_CONT_FK FOREIGN KEY (CONTRACT_ID) REFERENCES APP_PBE.CONTRACTS (ID);
COMMENT ON COLUMN APP_PBE.ORDERS.CONTRACT_ID IS 'Identyfikator umowy';
ALTER TABLE APP_PBE.ORDERS ADD (GRANT_PROGRAM_ID NUMBER);
ALTER TABLE APP_PBE.ORDERS ADD CONSTRAINT ORD_GRANT_FK FOREIGN KEY (GRANT_PROGRAM_ID) REFERENCES APP_PBE.GRANT_PROGRAMS (ID);
COMMENT ON COLUMN APP_PBE.ORDERS.GRANT_PROGRAM_ID IS 'Identyfikator programu dofinansowania';

UPDATE APP_PBE.ORDERS o SET o.GRANT_PROGRAM_ID = (SELECT g.GRANT_PROGRAM_ID
                  FROM APP_PBE.GRANT_APPLICATIONS g
                  WHERE g.ID = o.GRANT_APPLICATION_ID)
WHERE o.GRANT_APPLICATION_ID IS NOT NULL;

UPDATE APP_PBE.ORDERS o
SET o.GRANT_PROGRAM_ID = (SELECT c.GRANT_PROGRAM_ID
                  FROM APP_PBE.CONTRACTS c
                  WHERE c.ID = o.CONTRACT_ID)
WHERE o.CONTRACT_ID IS NOT NULL;

ALTER TABLE APP_PBE.ORDERS MODIFY GRANT_PROGRAM_ID NUMBER NOT NULL;

CREATE SEQUENCE EAGLE.ORDER_SEQ
MINVALUE 1
MAXVALUE 9999999999999999999999999999
INCREMENT BY 1
START WITH 3000
CACHE 20
NOORDER NOCYCLE;

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE', 'ORDER_SEQ', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'DEVELOPER', 'ORDER_SEQ', 'SELECT'));
END;
/

ALTER TABLE APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS ADD (AUTOMATIC VARCHAR2(1 BYTE) default 'N');
UPDATE APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS SET AUTOMATIC = 'N';
ALTER TABLE APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS MODIFY AUTOMATIC NOT NULL;




CREATE TABLE APP_PBE.ORDER_FLOWS_FOR_GR_PROGRAMS
(
  ID NUMBER NOT NULL,
  GRANT_PROGRAM_ID NUMBER NOT NULL,
  ORDER_FLOW_ID NUMBER NOT NULL,
  DATE_FROM DATE,
  DATE_TO DATE
);

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'ORDER_FLOWS_FOR_GR_PROGRAMS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'ORDER_FLOWS_FOR_GR_PROGRAMS', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'ORDER_FLOWS_FOR_GR_PROGRAMS', 'SELECT'));
END;
/

-- Klucze
CREATE UNIQUE INDEX APP_PBE.ORD_FLO_FOR_GR_PRO_PK ON APP_PBE.ORDER_FLOWS_FOR_GR_PROGRAMS (ID);
ALTER TABLE APP_PBE.ORDER_FLOWS_FOR_GR_PROGRAMS ADD CONSTRAINT ORD_FLO_FOR_GR_PRO_PK PRIMARY KEY (ID);

-- Klucze obce
ALTER TABLE APP_PBE.ORDER_FLOWS_FOR_GR_PROGRAMS ADD CONSTRAINT ORD_FLO_FOR_GR_PRO_GRAPRO_FK FOREIGN KEY (GRANT_PROGRAM_ID) REFERENCES APP_PBE.GRANT_PROGRAMS (ID);
ALTER TABLE APP_PBE.ORDER_FLOWS_FOR_GR_PROGRAMS ADD CONSTRAINT ORD_FLO_FOR_GR_PRO_ORDFLO_FK FOREIGN KEY (ORDER_FLOW_ID) REFERENCES APP_PBE.ORDER_FLOWS (ID);

-- Komentarze
COMMENT ON TABLE APP_PBE.ORDER_FLOWS_FOR_GR_PROGRAMS IS '@Author(Tomasz Bilski); @Project(GryfBonEl); @Date(2016-11-05);@Purpose(Tabel ��cz�ca program dofinansowania i przep�yw zam�wienia);';
COMMENT ON COLUMN APP_PBE.ORDER_FLOWS_FOR_GR_PROGRAMS.ID IS 'Klucz g��wny';
COMMENT ON COLUMN APP_PBE.ORDER_FLOWS_FOR_GR_PROGRAMS.GRANT_PROGRAM_ID IS 'Indetyfikator programu dofinansowania';
COMMENT ON COLUMN APP_PBE.ORDER_FLOWS_FOR_GR_PROGRAMS.ORDER_FLOW_ID IS 'Indetyfikator przep�ywu zam�wienia';
COMMENT ON COLUMN APP_PBE.ORDER_FLOWS_FOR_GR_PROGRAMS.DATE_FROM IS 'Data od kt�rej obowi�zywuje dane po��czenie';
COMMENT ON COLUMN APP_PBE.ORDER_FLOWS_FOR_GR_PROGRAMS.DATE_TO IS 'Data do kt�rej obowi�zywuje dane po��czenie';



ALTER TABLE APP_PBE.ORDERS ADD (EXTERNAL_ORDER_ID VARCHAR2(20 BYTE));
COMMENT ON COLUMN APP_PBE.ORDERS.EXTERNAL_ORDER_ID IS 'Identyfikator zewnetrzny zam�wienia';



CREATE TABLE APP_PBE.ORDER_INVOICES
(
  ID NUMBER NOT NULL,
  ORDER_ID NUMBER NOT NULL,
  INVOICE_ID NUMBER NOT NULL,
  INVOICE_NUMBER VARCHAR2(30 BYTE) NOT NULL,
  INVOICE_DATE DATE NOT NULL,
  VERSION NUMBER NOT NULL,
  CREATED_USER VARCHAR2(30 BYTE) NOT NULL,
  CREATED_TIMESTAMP TIMESTAMP(6) NOT NULL,
  MODIFIED_USER VARCHAR2(30 BYTE) NOT NULL,
  MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL
);

BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'EAGLE', 'ORDER_INVOICES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'ORDER_INVOICES', 'ALL'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'DEVELOPER', 'ORDER_INVOICES', 'SELECT'));
END;
/

-- Klucze
CREATE UNIQUE INDEX APP_PBE.ORD_INV_PK ON APP_PBE.ORDER_INVOICES (ID);
ALTER TABLE APP_PBE.ORDER_INVOICES ADD CONSTRAINT ORD_INV_PK PRIMARY KEY (ID);

--KLUCZE OBCE
ALTER TABLE APP_PBE.ORDER_INVOICES ADD CONSTRAINT ORD_INV_ORD_FK FOREIGN KEY (ORDER_ID) REFERENCES APP_PBE.ORDERS (ID);


COMMENT ON TABLE APP_PBE.ORDER_INVOICES IS '@Author(Tomasz Bilski); @Project(GryfBonEl); @Date(2016-11-05);@Purpose(Tabel warto�ci faktur dla um�w);';
COMMENT ON COLUMN APP_PBE.ORDER_INVOICES.ID IS 'Identyfikato klucz sztuczny';
COMMENT ON COLUMN APP_PBE.ORDER_INVOICES.ORDER_ID IS 'Identyfikator zam�wienia';
COMMENT ON COLUMN APP_PBE.ORDER_INVOICES.INVOICE_ID IS 'Identyfikator umowy';
COMMENT ON COLUMN APP_PBE.ORDER_INVOICES.INVOICE_NUMBER IS 'Numer umowy';
COMMENT ON COLUMN APP_PBE.ORDER_INVOICES.INVOICE_DATE IS 'Data umowy';
COMMENT ON COLUMN APP_PBE.ORDER_INVOICES.VERSION IS 'Wersja. Kolumna techniczna u�ywana do mechanizmu Optimistic Lock';
COMMENT ON COLUMN APP_PBE.ORDER_INVOICES.CREATED_USER IS 'Login u�ytkownika, kt�ry utworzy� rekord';
COMMENT ON COLUMN APP_PBE.ORDER_INVOICES.CREATED_TIMESTAMP IS 'Data utworzenia rekordu';
COMMENT ON COLUMN APP_PBE.ORDER_INVOICES.MODIFIED_USER IS 'Login u�ytkownika, kt�ry ostatnio modyfikowa� rekord';
COMMENT ON COLUMN APP_PBE.ORDER_INVOICES.MODIFIED_TIMESTAMP IS 'Data ostatniej modyfikacji rekordu';


alter table APP_PBE.GRANT_PROGRAMS add(PROGRAM_CODE VARCHAR2(10));
COMMENT ON COLUMN APP_PBE.GRANT_PROGRAMS.PROGRAM_CODE IS 'Kod programu na potrzeby m. in. komunikacji z NAVISION.';

