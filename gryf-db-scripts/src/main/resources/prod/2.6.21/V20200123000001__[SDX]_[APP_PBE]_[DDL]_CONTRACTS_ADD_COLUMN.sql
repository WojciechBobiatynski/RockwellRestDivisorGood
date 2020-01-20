-- DODANIE KOLUMNY Z PROCENTEM W KONTRAKCIE
ALTER TABLE APP_PBE.CONTRACTS
ADD (OWN_CONTRIBUTION_PERCENTAGE NUMBER(8,2));

COMMENT ON COLUMN 
APP_PBE.CONTRACTS.OWN_CONTRIBUTION_PERCENTAGE IS 
'Procent kwoty wk�adu w�asnego';

-- DODANIE KOLUMNY Z PROCENTEM W AUDYCIE KONTRAKT�W
ALTER TABLE APP_PBE.CONTRACTS_AUDIT
ADD (OWN_CONTRIBUTION_PERCENTAGE NUMBER(8,2));

-- TRIGGER DO KONTAKT�W
CREATE OR REPLACE TRIGGER APP_PBE.TRG_CONTRACTS_AUDIT
AFTER DELETE OR UPDATE ON APP_PBE.CONTRACTS
REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW
DECLARE
  v_operation  VARCHAR2(1) := 'U';
  v_user       VARCHAR2(100) := USER;
BEGIN
 IF DELETING THEN v_operation := 'D'; END IF;
 IF USER in ( 'SRV_EE', 'SRV_EE_TI', 'SRV_EE_IND' ) AND NOT DELETING THEN v_user := :NEW.MODIFIED_USER; END IF; -- w przypadku GRYF-a jest pula połączeń na użytkowniku SRV_EE
  INSERT INTO APP_PBE.CONTRACTS_AUDIT a (
    ID,
    CODE,
    ACCOUNT_PAYMENT,
    CONTRACT_TYPE_ID,
    INDIVIDUAL_ID,
    ENTERPRISE_ID,
    GRANT_PROGRAM_ID,
    SIGN_DATE,
    EXPIRY_DATE,
    VERSION,
    CREATED_USER,
    CREATED_TIMESTAMP,
    MODIFIED_USER,
    MODIFIED_TIMESTAMP,
    AUDIT_USER,AUDIT_TIMESTAMP,AUDIT_OPERATION,
    ADDRESS_INVOICE,
    ZIP_CODE_INVOICE,
    ADDRESS_CORR,
    ZIP_CODE_CORR,
	OWN_CONTRIBUTION_PERCENTAGE
    )
  VALUES (
    :OLD.ID,
    :OLD.CODE,
    :OLD.ACCOUNT_PAYMENT,
    :OLD.CONTRACT_TYPE_ID,
    :OLD.INDIVIDUAL_ID,
    :OLD.ENTERPRISE_ID,
    :OLD.GRANT_PROGRAM_ID,
    :OLD.SIGN_DATE,
    :OLD.EXPIRY_DATE,
    :OLD.VERSION,
    :OLD.CREATED_USER,
    :OLD.CREATED_TIMESTAMP,
    :OLD.MODIFIED_USER,
    :OLD.MODIFIED_TIMESTAMP,
    v_user, SYSTIMESTAMP, v_operation ,
    :OLD.ADDRESS_INVOICE,
    :OLD.ZIP_CODE_INVOICE,
    :OLD.ADDRESS_CORR,
    :OLD.ZIP_CODE_CORR,
	:OLD.OWN_CONTRIBUTION_PERCENTAGE);
EXCEPTION
  WHEN OTHERS THEN
    EAGLE.PK_ERROR.RAISE_ERROR(sqlcode,sqlerrm,'TRG_CONTRACTS_AUDIT');
END TRG_CONTRACTS_AUDIT;
/