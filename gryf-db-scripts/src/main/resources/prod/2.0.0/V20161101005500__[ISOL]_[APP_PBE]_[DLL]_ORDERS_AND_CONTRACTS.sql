ALTER TABLE APP_PBE.ORDERS ADD (CONTRACT_ID NUMBER);
ALTER TABLE APP_PBE.ORDERS ADD CONSTRAINT ORD_CONT_FK FOREIGN KEY (CONTRACT_ID) REFERENCES APP_PBE.CONTRACTS (ID);
COMMENT ON COLUMN APP_PBE.ORDERS.CONTRACT_ID IS 'Identyfikator umowy';
ALTER TABLE APP_PBE.ORDERS ADD (GRANT_PROGRAM_ID NUMBER);
ALTER TABLE APP_PBE.ORDERS ADD CONSTRAINT ORD_GRANT_FK FOREIGN KEY (GRANT_PROGRAM_ID) REFERENCES APP_PBE.GRANT_PROGRAMS (ID);
COMMENT ON COLUMN APP_PBE.ORDERS.GRANT_PROGRAM_ID IS 'Identyfikator programu dofinansowania';

UPDATE APP_PBE.ORDERS o
SET o.GRANT_PROGRAM_ID = (SELECT g.GRANT_PROGRAM_ID
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






