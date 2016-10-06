-- PRIV & ROLE
INSERT INTO APP_PBE.GRF_PRIVILEGES (CODE,DESCRIPTION,CONTEXT) VALUES('TRIN_TEST_PRIV','Przywilej testowy dla instytucji szkoleniowej','TRIN');
INSERT INTO APP_PBE.GRF_ROLES(CODE,DESCRIPTION,CONTEXT)VALUES('TRIN_TEST_ROLE','Rola testowa dla instytucji szkoleniowej','TRIN');

INSERT INTO APP_PBE.GRF_PRIVS_IN_ROLE (GRF_PRIV_CODE, GRF_ROLE_CODE) VALUES('TRIN_TEST_PRIV','TRIN_TEST_ROLE');

-- USER
-- user testowy  
-- login : trin_usr_test
-- pass: trin_usr_test

DECLARE
TRIN_ID number;
USR_ID number;
BEGIN

TRIN_ID := TRIN_SEQ.nextval;
USR_ID := TRIN_USR_SEQ.nextval;

  INSERT INTO APP_PBE.TRAINING_INSTITUTIONS
      (ID, CODE,
      ZIP_CODE_INVOICE, ZIP_CODE_CORR,
      NAME, VAT_REG_NUM, ADDRESS_INVOICE, ADDRESS_CORR, 
      VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP )
    VALUES
      (TRIN_ID, 'TEST',
      (SELECT ID FROM EAGLE.ZIP_CODES WHERE ACTIVE =1 AND ROWNUM <= 1), (SELECT ID FROM EAGLE.ZIP_CODES WHERE ACTIVE =1 AND ROWNUM <= 1), 
      'Instutucja dla użytkownika testowego' ,'3554868937', 'adres','adres_kor',
      1, user, sysdate, user,sysdate);
      
  INSERT INTO APP_PBE.TRAINING_INSTITUTION_USERS
      ( ID, TRAINING_ISTITUTION_ID, LOGIN, EMAIL, PASSWORD,
        VERSION,CREATED_USER,CREATED_TIMESTAMP, MODIFIED_USER,MODIFIED_TIMESTAMP
      )
    VALUES
      ( USR_ID, TRIN_ID, 'trin_usr_test' , 'test@isolution.pl', '$2a$04$2rzdu6A74ivGvWPV4ywDnu8PZr7WqLKYb9.UPw5KCUY4jy5UYaudC', 
      1, user, sysdate, user,sysdate);
  
 
  INSERT INTO APP_PBE.TRIN_USERS_IN_ROLE( TRIN_USER_ID, TRIN_GRF_ROLE_CODE  )
    VALUES(USR_ID,'TRIN_TEST_ROLE');
END;

