-- Part 1:

CREATE TABLE APP_PBE.TI_TRAINING_INSTANCES_EXT (
  TI_EXTERNAL_ID        VARCHAR2(40),
  VAT_REG_NUM           VARCHAR2(20 BYTE),
  TI_EXTERNAL_NAME      VARCHAR2(500 BYTE),
  TRAINING_EXTERNAL_ID  VARCHAR2(30 BYTE),
  TRAINING_NAME         VARCHAR2(200 BYTE),
  START_DATE_STR        VARCHAR2(200 BYTE),
  END_DATE_STR          VARCHAR2(200 BYTE),
  PLACE                 VARCHAR2(200 BYTE),
  Price_STR             VARCHAR2(200 BYTE),
  HOURS_NUMBER_STR      VARCHAR2(200 BYTE),
  HOUR_PRICE_STR        VARCHAR2(200 BYTE),
  TRAINING_CATEGORY_ID  VARCHAR2(5 BYTE),
  CERTIFICATE_REMARK    VARCHAR2(200),
  IND_ORDER_EXTERNAL_ID VARCHAR2(200),
  ROW_NUM               NUMBER(10),
  IMPORT_ID             NUMBER,
  ORD_SEARCH_STRING     VARCHAR2(100),
  START_DATE            DATE,
  END_DATE              DATE,
  Price                 NUMBER(10, 3),
  HOURS_NUMBER          NUMBER(8, 2),
  HOUR_PRICE            NUMBER(10, 3),
  CREATED_USER          VARCHAR2(100),
  CREATED_TIMESTAMP     TIMESTAMP(6)
);

CREATE SEQUENCE APP_PBE.TITE_SEQ
START WITH 1
INCREMENT BY 1;

COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.IMPORT_ID IS 'Identyfikator importu';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.ROW_NUM IS 'Numer linii';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.TI_EXTERNAL_ID IS 'IS - zewnętrzne id';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.VAT_REG_NUM IS 'NIP';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.TI_EXTERNAL_NAME IS 'IS - nazwa';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.TRAINING_EXTERNAL_ID IS 'Uługa - Zewnętrzny identyfikator';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.TRAINING_NAME IS 'Uługa - Nazwa ';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.START_DATE IS 'Uługa - Data rozpoczęcia  ';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.END_DATE IS 'Uługa - Data zakonczenia  ';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.PLACE IS 'Uługa - Miejsce usługi  ';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.Price IS 'Uługa - Cena usługi (PLN)  ';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.HOURS_NUMBER IS 'Uługa - Ilośc godzin  ';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.HOUR_PRICE IS 'Uługa - Cena 1h usługi  ';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.TRAINING_CATEGORY_ID IS 'Uługa - Kategoria';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.CERTIFICATE_REMARK IS 'Certyfikat - opis  ';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.IND_ORDER_EXTERNAL_ID IS 'Identyfikator wsparcia';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.CREATED_USER IS 'Użytkownik, który ustworzył';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.CREATED_TIMESTAMP IS 'Data utworzenia';
COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.ORD_SEARCH_STRING IS 'Ciąg znaków wykorzystywany do wyszukania numeru zamówienia';

-- Part 2:


ALTER TABLE APP_PBE.TI_TRAINING_INSTANCES_EXT
  ADD ID NUMBER(10);


ALTER TABLE APP_PBE.TI_TRAINING_INSTANCES_EXT
  ADD
  TRAINING_ID NUMBER;


COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.TRAINING_ID IS 'ID szkolenia';

ALTER TABLE APP_PBE.TI_TRAINING_INSTANCES_EXT
  ADD
  CONSTRAINT PBE_TTIE_INS_TRA_FK
  FOREIGN KEY (TRAINING_ID)
  REFERENCES APP_PBE.TI_TRAININGS (ID)
  ENABLE VALIDATE;

ALTER TABLE APP_PBE.TI_TRAINING_INSTANCES_EXT
  ADD IMPORT_JOB_ID NUMBER;

COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.TRAINING_ID IS 'ID job, w ramach którego nastąpił import';

ALTER TABLE APP_PBE.TI_TRAINING_INSTANCES_EXT
  ADD
  CONSTRAINT PBE_TTIE_ASYN_JOB_FK
  FOREIGN KEY (IMPORT_JOB_ID)
  REFERENCES APP_PBE.TI_TRAININGS (ID)
  ENABLE VALIDATE;


ALTER TABLE APP_PBE.TI_TRAINING_INSTANCES_EXT
  ADD
  VERSION NUMBER;

COMMENT ON COLUMN APP_PBE.TI_TRAINING_INSTANCES_EXT.TRAINING_ID IS 'Numer wersji rekordu';


ALTER TABLE APP_PBE.TI_TRAINING_INSTANCES_EXT
  ADD -- NOT NULL
  MODIFIED_USER VARCHAR2(100 BYTE);


ALTER TABLE APP_PBE.TI_TRAINING_INSTANCES_EXT
  ADD -- NOT NULL
  MODIFIED_TIMESTAMP TIMESTAMP(6);


DECLARE
  v_x VARCHAR2(100);
BEGIN
  v_x := SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE', 'TI_TRAINING_INSTANCES_EXT', 'ALL');
  dbms_output.put_line(v_x);
END;

