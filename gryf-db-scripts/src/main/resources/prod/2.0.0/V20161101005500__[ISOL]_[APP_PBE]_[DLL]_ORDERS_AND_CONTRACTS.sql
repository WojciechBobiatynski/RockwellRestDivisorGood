ALTER TABLE APP_PBE.ORDERS ADD (CONTRACT_ID NUMBER);
ALTER TABLE APP_PBE.ORDERS ADD CONSTRAINT ORD_CONT_FK FOREIGN KEY (CONTRACT_ID) REFERENCES APP_PBE.CONTRACTS (ID);
COMMENT ON COLUMN APP_PBE.ORDERS.CONTRACT_ID IS 'Identyfikator umowy';

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

