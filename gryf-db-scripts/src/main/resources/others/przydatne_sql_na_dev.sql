update EAGLE.ADM_PARAMETERS
set value = 'C:\Users\Isolution\Desktop\GryfWorkspace\GryfFileRepo\'
WHERE NAME = 'GRYF_PATH_ATTACHMENTS';



update APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS
set AUTOMATIC = 'N'
where ACTION_ID in (10045, 10034, 10023);


update APP_PBE.GRANT_PROGRAM_LIMITS
set LIMIT_VALUE = 20
where GRANT_PROGRAM_ID = 100 and LIMIT_TYPE = 'ORDNUMLIM';

delete
from APP_FIN.INVOICE_LINES
where INV_ID = 814 and (POS_NUM <> 1 or POS_TYPE <> 'S');


MERGE INTO EAGLE.ADM_PARAMETERS ug USING (
                                           SELECT
                                             'GRYF_TI_USER_URL' NAME,
                                             'http://localhost:8080/gryf-ti-fo/' VALUE,
                                             'URL aplikacji TI' DESCRIPTION
                                           FROM dual
                                         ) ins
ON (ug.NAME = ins.NAME)
WHEN MATCHED THEN
UPDATE SET ug.VALUE = ins.VALUE, ug.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED THEN INSERT
  (NAME,VALUE,DESCRIPTION)
VALUES (ins.NAME, ins.VALUE, ins.DESCRIPTION);



MERGE INTO EAGLE.ADM_PARAMETERS ug USING (
                                           SELECT
                                             'GRYF_IND_USER_URL' NAME,
                                             'http://localhost:8080/gryf-ind-fo/' VALUE,
                                             'URL aplikacji IND' DESCRIPTION
                                           FROM dual
                                         ) ins
ON (ug.NAME = ins.NAME)
WHEN MATCHED THEN
UPDATE SET ug.VALUE = ins.VALUE, ug.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED THEN INSERT
  (NAME,VALUE,DESCRIPTION)
VALUES (ins.NAME, ins.VALUE, ins.DESCRIPTION);

update APP_PBE.TI_TRAININGS
set ACTIVE = 'Y',
  DEACTIVATE_USER = null,
  DEACTIVATE_DATE = null,
  DEACTIVATE_JOB_ID = null;





---------------------------------------------------------------------------
--- APP_FIN.NAV_MC_TRANSFERS_PRC
---------------------------------------------------------------------------
CREATE TABLE APP_FIN.NAV_MC_TRANSFERS_PRC
(
  ID                NUMBER(10)                  NOT NULL,
  USED              VARCHAR2(1 BYTE),
  ORDER_ID          VARCHAR2(20 BYTE),
  SOURCE_TYPE       VARCHAR2(10),
  DOC_NUMBER        VARCHAR2(20 BYTE)           NOT NULL,
  AMOUNT            NUMBER(16,2)                NOT NULL,
  CUST_NAME         VARCHAR2(50 BYTE),
  CUST_BALANCE      NUMBER(12,2),
  MATCH_DATE        DATE,
  MATCH_DATE_NAV    DATE,
  PAYMENT_DATE      DATE                        NOT NULL,
  TRANSFER_ID       NUMBER(10),
  TRANSFER_DATE     DATE,
  TRANSFER_DETAIL   VARCHAR2(300 BYTE),
  TRANSFER_AMOUNT   NUMBER(12,2),
  ERROR_TIMESTAMP   TIMESTAMP,
  ERROR_MESSAGE     VARCHAR2(1000)
);

COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.ID                         IS 'Id Pobierane z sekwencji...';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.USED                       IS 'Czy wpis zosta³ u¿yty do oznaczenia p³atnoœci na docelowym obiekcie (zamówieniu/Zadaniu/Zamówieniu PB';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.ORDER_ID                   IS 'Id Zamówienia / Zadania / Zamówienia PB';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.SOURCE_TYPE                IS 'Typ Ÿród³a identyfikatora w polu ORD_ID
RECK - Rozliczenie akceptacji kart
ORD - Zamówienie
REM - Rozliczenie
TSK - Zadanie
ORDV - Zamówienie VB
IP - Propozycja faktury
ORDPB - Zamówienia PB';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.DOC_NUMBER                 IS 'Identyfikator dokumentu, po którym nast¹pi³o zmatchowanie p³atnoœci. Dla zamówieñ, zadañ, propozycji faktury jest to numer proformy. Dla zamówieñ PB jest to zewnêtrzny numer zamówienia nadawany przez WUP';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.AMOUNT                     IS 'Wartoœæ podpiêta do zamówienia';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.CUST_NAME                  IS 'Nazwa klienta';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.CUST_BALANCE               IS 'Bilans klienta';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.MATCH_DATE                 IS 'Data podpiêcia p³atnoœci w Eagle';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.MATCH_DATE_NAV             IS 'Data podpiêcia p³atnoœci w Navision';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.PAYMENT_DATE               IS 'Data p³atnoœci';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.TRANSFER_ID                IS 'Identyfikator przelewu';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.TRANSFER_DATE              IS 'Data przelewu';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.TRANSFER_DETAIL            IS 'Tytu³ przelewu';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.TRANSFER_AMOUNT            IS 'Kwota przelewu';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.ERROR_TIMESTAMP            IS 'B³¹d podpiêcia p³atnoœci - data';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.ERROR_MESSAGE              IS 'B³¹d podpiêcia p³atnoœci - opis';

CREATE SEQUENCE EAGLE.NATP_SEQ
START WITH 1
INCREMENT BY 1;

ALTER TABLE APP_FIN.NAV_MC_TRANSFERS_PRC ADD (
  CONSTRAINT NATP_PK
  PRIMARY KEY   (ID)
    USING INDEX
      ENABLE VALIDATE);

BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_FIN', 'EAGLE', 'NAV_MC_TRANSFERS_PRC', 'ALL'));
END;
/


ALTER TABLE APP_FIN.NAV_MC_TRANSFERS_PRC ADD
  (
  CREATED_USER          VARCHAR2(30 BYTE)       DEFAULT USER  NOT NULL ,
  CREATED_TIMESTAMP     TIMESTAMP(6)            DEFAULT SYSTIMESTAMP  NOT NULL,
  MODIFIED_USER         VARCHAR2(30 BYTE)       DEFAULT USER  NOT NULL,
  MODIFIED_TIMESTAMP    TIMESTAMP(6)            DEFAULT SYSTIMESTAMP NOT NULL
  );

COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.CREATED_USER       IS 'U¿ytkownik tworzacy wiersz - kolumna audytowa';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.CREATED_TIMESTAMP  IS 'Timestamp utworzenia wiersza - kolumna audytowa';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.MODIFIED_USER      IS 'Ostatni u¿ytkownik modyfikuj¹cy wiersz - kolumna audytowa';
COMMENT ON COLUMN APP_FIN.NAV_MC_TRANSFERS_PRC.MODIFIED_TIMESTAMP IS 'Timestamp ostatniej modyfikacji wiersza - kolumna audytowa';


Insert into APP_FIN.NAV_MC_TRANSFERS_PRC (ID,USED,ORDER_ID,SOURCE_TYPE,DOC_NUMBER,AMOUNT,CUST_NAME,CUST_BALANCE,MATCH_DATE,MATCH_DATE_NAV,PAYMENT_DATE,TRANSFER_ID,TRANSFER_DATE,TRANSFER_DETAIL,TRANSFER_AMOUNT,ERROR_TIMESTAMP,ERROR_MESSAGE,CREATED_USER,CREATED_TIMESTAMP,MODIFIED_USER,MODIFIED_TIMESTAMP) values ('1','Y','1265','ORDPB','dcvsd','21,45','COS','1000',to_date('16/12/20','RR/MM/DD'),to_date('16/12/20','RR/MM/DD'),to_date('16/12/20','RR/MM/DD'),'123',to_date('16/12/19','RR/MM/DD'),'P³atnoœæ za dokument dcvsd','21,45',null,null,'EAGLE',to_timestamp('17/01/30 10:33:39,466000000','RR/MM/DD HH24:MI:SSXFF'),'EAGLE',to_timestamp('17/01/30 10:33:39,466000000','RR/MM/DD HH24:MI:SSXFF'));
Insert into APP_FIN.NAV_MC_TRANSFERS_PRC (ID,USED,ORDER_ID,SOURCE_TYPE,DOC_NUMBER,AMOUNT,CUST_NAME,CUST_BALANCE,MATCH_DATE,MATCH_DATE_NAV,PAYMENT_DATE,TRANSFER_ID,TRANSFER_DATE,TRANSFER_DETAIL,TRANSFER_AMOUNT,ERROR_TIMESTAMP,ERROR_MESSAGE,CREATED_USER,CREATED_TIMESTAMP,MODIFIED_USER,MODIFIED_TIMESTAMP) values ('2','Y','1265','ORDPB','WKK/50/1','19,5','COS','1000',to_date('16/12/20','RR/MM/DD'),to_date('16/12/20','RR/MM/DD'),to_date('16/12/20','RR/MM/DD'),'123',to_date('16/12/19','RR/MM/DD'),'P³atnoœæ za zamówienie PB WKK/50/1','19,5',null,null,'EAGLE',to_timestamp('17/01/30 10:33:39,466000000','RR/MM/DD HH24:MI:SSXFF'),'EAGLE',to_timestamp('17/01/30 10:33:39,466000000','RR/MM/DD HH24:MI:SSXFF'));

BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_FIN', 'SRV_EE', 'NAV_MC_TRANSFERS_PRC', 'SELECT'));
END;
/
