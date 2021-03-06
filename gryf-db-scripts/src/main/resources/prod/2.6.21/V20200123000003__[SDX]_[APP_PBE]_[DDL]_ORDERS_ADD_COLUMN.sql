-- ZAMOWIENIE
ALTER TABLE APP_PBE.ORDERS
ADD (OWN_CONTRIBUTION_PERCENTAGE NUMBER(8,2));

COMMENT ON COLUMN 
APP_PBE.ORDERS.OWN_CONTRIBUTION_PERCENTAGE IS 
'Procent kwoty wk?adu w?asnego';

-- ZAMOWIENIE - AUDYT
ALTER TABLE APP_PBE.ORDERS_AUDIT
ADD (OWN_CONTRIBUTION_PERCENTAGE NUMBER(8,2));

-- TRIGGER ZAMOWIENIE
CREATE OR REPLACE TRIGGER APP_PBE.TRG_ORDERS_AUDIT
AFTER DELETE OR UPDATE ON APP_PBE.ORDERS
REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  v_operation  VARCHAR2(1) := 'U';
  v_user       VARCHAR2(100) := USER; 
BEGIN 
 IF DELETING THEN v_operation := 'D'; END IF;
 IF USER in ( 'SRV_EE', 'SRV_EE_TI', 'SRV_EE_IND' ) AND NOT DELETING THEN v_user := :NEW.MODIFIED_USER; END IF; -- w przypadku GRYF-a jest pula po??cze? na u?ytkowniku SRV_EE
  INSERT INTO APP_PBE.ORDERS_AUDIT ( 
    ID,
    ORDER_FLOW_ID,
    GRANT_APPLICATION_ID,
    ENTERPRISE_ID,
    STATUS_ID,
    OPERATOR_ID,
    ORDER_DATE,
    VOUCHERS_NUMBER,
    ADDRESS_CORR,
    ZIP_CODE_CORR_ID,
    REMARKS,
    VERSION,
    CREATED_USER,
    CREATED_TIMESTAMP,
    MODIFIED_USER,
    MODIFIED_TIMESTAMP,
    PRODUCT_ID,
    CONTRACT_ID,
    GRANT_PROGRAM_ID,
    EXTERNAL_ORDER_ID,
    PBE_PRD_ID,
    AUDIT_USER,AUDIT_TIMESTAMP,AUDIT_OPERATION,
	OWN_CONTRIBUTION_PERCENTAGE)
  VALUES (
    :OLD.ID,
    :OLD.ORDER_FLOW_ID,
    :OLD.GRANT_APPLICATION_ID,
    :OLD.ENTERPRISE_ID,
    :OLD.STATUS_ID,
    :OLD.OPERATOR_ID,
    :OLD.ORDER_DATE,
    :OLD.VOUCHERS_NUMBER,
    :OLD.ADDRESS_CORR,
    :OLD.ZIP_CODE_CORR_ID,
    :OLD.REMARKS,
    :OLD.VERSION,
    :OLD.CREATED_USER,
    :OLD.CREATED_TIMESTAMP,
    :OLD.MODIFIED_USER,
    :OLD.MODIFIED_TIMESTAMP,
    :OLD.PRODUCT_ID,
    :OLD.CONTRACT_ID,
    :OLD.GRANT_PROGRAM_ID,
    :OLD.EXTERNAL_ORDER_ID,
    :OLD.PBE_PRD_ID,
    v_user, SYSTIMESTAMP, v_operation,
    :OLD.OWN_CONTRIBUTION_PERCENTAGE );
EXCEPTION 
  WHEN OTHERS THEN 
    EAGLE.PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'TRG_ORDERS_AUDIT');
END TRG_ORDERS_AUDIT;
/