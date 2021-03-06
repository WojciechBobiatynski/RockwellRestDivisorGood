CREATE OR REPLACE TRIGGER APP_PBE.TRG_BU_IND_ACCOUNT
BEFORE INSERT OR UPDATE
ON APP_PBE.INDIVIDUALS
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN

  IF INSERTING OR UPDATING('CODE') THEN
    IF length(:new.code) = 8 AND :new.ACCOUNT_PAYMENT is null THEN
      :NEW.account_payment := eagle.t$bank_account.GET_COR_SPP(:new.code);
    END IF;
  END IF;

END TRG_BU_IND_ACCOUNT;

-- Create indexes for table APP_PBE.INDIVIDUALS