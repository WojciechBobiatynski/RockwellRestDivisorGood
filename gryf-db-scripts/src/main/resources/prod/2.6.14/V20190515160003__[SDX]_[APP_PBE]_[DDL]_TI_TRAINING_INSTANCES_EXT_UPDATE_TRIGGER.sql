
CREATE OR REPLACE TRIGGER ${gryf.schema}.TRG_TI_TRAINING_INST_EXT_AUDIT
   AFTER DELETE OR UPDATE
   ON ${gryf.schema}.TI_TRAINING_INSTANCES_EXT
   REFERENCING NEW AS NEW OLD AS OLD
   FOR EACH ROW
DECLARE
   v_operation   VARCHAR2 (1) := 'U';
   v_user        VARCHAR2 (30) := USER;
BEGIN
   IF    DELETING
      OR :new.TI_EXTERNAL_ID != :old.TI_EXTERNAL_ID
      OR :new.VAT_REG_NUM != :old.VAT_REG_NUM
      OR :new.TI_EXTERNAL_NAME != :old.TI_EXTERNAL_NAME
      OR :new.TRAINING_EXTERNAL_ID != :old.TRAINING_EXTERNAL_ID
      OR :new.TRAINING_NAME != :old.TRAINING_NAME
      OR :new.PLACE != :old.PLACE
      OR :new.TRAINING_CATEGORY_ID != :old.TRAINING_CATEGORY_ID
      OR :new.CERTIFICATE_REMARK != :old.CERTIFICATE_REMARK
      OR :new.IND_ORDER_EXTERNAL_ID != :old.IND_ORDER_EXTERNAL_ID
      OR :new.START_DATE != :old.START_DATE
      OR :new.END_DATE != :old.END_DATE
      OR :new.PRICE != :old.PRICE
      OR :new.HOURS_NUMBER != :old.HOURS_NUMBER
      OR :new.HOUR_PRICE != :old.HOUR_PRICE
      OR :new.TRAINING_ID != :old.TRAINING_ID
      OR :new.SUBCATEGORY_NAME != :old.SUBCATEGORY_NAME
      OR :new.EXAM != :old.EXAM
      OR :new.CERTIFICATE != :old.CERTIFICATE
      OR :new.STATUS != :old.STATUS
      OR :new.QUALIFICATION != :old.QUALIFICATION
      OR :new.OTHER_QUALIFICATION != :old.OTHER_QUALIFICATION
      OR :new.QUALIFICATION_CODE != :old.QUALIFICATION_CODE
      OR :new.REGISTRATION_DATE != :old.REGISTRATION_DATE
      OR :new.MAX_PARTICIPANTS_COUNT != :old.MAX_PARTICIPANTS_COUNT
      OR :new.PRICE_VALIDATE_TYPE != :old.PRICE_VALIDATE_TYPE
   THEN
      IF DELETING
      THEN
         v_operation := 'D';
      END IF;

      IF USER IN ('SRV_EE', 'SRV_EE_TI', 'SRV_EE_IND') AND NOT DELETING
      THEN
         v_user := :NEW.MODIFIED_USER;
      END IF;   -- w przypadku GRYF-a jest pula po³¹czeñ na u¿ytkowniku SRV_EE

      INSERT
        INTO ${gryf.schema}.TI_TRAINING_INSTANCES_EXT_AUDT (TI_EXTERNAL_ID,
                                                     VAT_REG_NUM,
                                                     TI_EXTERNAL_NAME,
                                                     TRAINING_EXTERNAL_ID,
                                                     TRAINING_NAME,
                                                     PLACE,
                                                     TRAINING_CATEGORY_ID,
                                                     CERTIFICATE_REMARK,
                                                     IND_ORDER_EXTERNAL_ID,
                                                     START_DATE,
                                                     END_DATE,
                                                     PRICE,
                                                     HOURS_NUMBER,
                                                     HOUR_PRICE,
                                                     CREATED_USER,
                                                     CREATED_TIMESTAMP,
                                                     ID,
                                                     TRAINING_ID,
                                                     IMPORT_JOB_ID,
                                                     VERSION,
                                                     MODIFIED_USER,
                                                     MODIFIED_TIMESTAMP,
                                                     SUBCATEGORY_NAME,
                                                     EXAM,
                                                     CERTIFICATE,
                                                     STATUS,
                                                     QUALIFICATION,
                                                     OTHER_QUALIFICATION,
                                                     QUALIFICATION_CODE,
                                                     REGISTRATION_DATE,
                                                     MAX_PARTICIPANTS_COUNT,
                                                     PRICE_VALIDATE_TYPE,
                                                     AUDIT_USER,
                                                     AUDIT_TIMESTAMP,
                                                     AUDIT_OPERATION)
      VALUES (:OLD.TI_EXTERNAL_ID,
              :OLD.VAT_REG_NUM,
              :OLD.TI_EXTERNAL_NAME,
              :OLD.TRAINING_EXTERNAL_ID,
              :OLD.TRAINING_NAME,
              :OLD.PLACE,
              :OLD.TRAINING_CATEGORY_ID,
              :OLD.CERTIFICATE_REMARK,
              :OLD.IND_ORDER_EXTERNAL_ID,
              :OLD.START_DATE,
              :OLD.END_DATE,
              :OLD.PRICE,
              :OLD.HOURS_NUMBER,
              :OLD.HOUR_PRICE,
              :OLD.CREATED_USER,
              :OLD.CREATED_TIMESTAMP,
              :OLD.ID,
              :OLD.TRAINING_ID,
              :OLD.IMPORT_JOB_ID,
              :OLD.VERSION,
              :OLD.MODIFIED_USER,
              :OLD.MODIFIED_TIMESTAMP,
              :old.SUBCATEGORY_NAME,
              :old.EXAM,
              :old.CERTIFICATE,
              :old.STATUS,
              :old.QUALIFICATION,
              :old.OTHER_QUALIFICATION,
              :old.QUALIFICATION_CODE,
              :old.REGISTRATION_DATE,
              :old.MAX_PARTICIPANTS_COUNT,
              :old.PRICE_VALIDATE_TYPE,
              v_user,
              SYSTIMESTAMP,
              v_operation);
   END IF;
EXCEPTION
   WHEN OTHERS
   THEN
      ${eagle.schema}.PK_ERROR.RAISE_ERROR (SQLCODE,
                                  SQLERRM,
                                  'TRG_TI_TRAINING_INST_EXT_AUDIT');
END TRG_TI_TRAINING_INST_EXT_AUDIT;
/