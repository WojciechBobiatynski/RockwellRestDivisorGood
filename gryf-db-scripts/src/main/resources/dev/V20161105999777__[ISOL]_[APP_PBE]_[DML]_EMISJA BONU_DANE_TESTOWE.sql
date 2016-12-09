
Insert into APP_STC.PRODUCT_TYPES (TYPE,NAME,PRD_GROUP,CALCULATING,REMARK,PARENT_TYPE,COMPOSIT,BANK_CODE,ACTIVE,WEIGHT)
values ('30','BON ELEKTRONICZNY','V','I',null,null,'N',null,'Y',0);
insert into APP_PBE.PBE_PRODUCTS (PRD_ID, PRD_TYPE, GRANT_OWNER_ID, NAME, VALUE, EXPIRY_DATE, DESCRIPTION, CREATION_DATE)
values ('KK_BON_ELE', '30', 100, 'Kierunek Kariera - Bon Elektroniczny', 15, null, 'Kierunek Kariera - Bon Elektroniczny', SYSDATE);



Insert into APP_PBE.GRANT_OWNER_AID_PRODUCTS (ID,GRANT_OWNER_ID,NAME,DESCRIPTION)
values ('BONELEKK',100,'BONY KIERUNEK KARIERA','Bony dla WUP Krakow - Kierunek Kariera');
Insert into APP_PBE.GRANT_PROGRAM_PRODUCTS (ID,GRANT_PROGRAM_ID,AID_PRODUCT_ID,PRD_ID,PBE_PRD_ID, DATE_FROM,DATE_TO)
values (2,100, 'BONELEKK',null, 'KK_BON_ELE', null,null);



insert into APP_PBE.PBE_PRODUCT_EMISSIONS (ID, PRD_ID, GRANT_PROGRAM_ID, NAME, DESCRIPTION, EMISSION_DATE, QUANTITY)
values ('KK_BON_ELE_EMIS_01', 'KK_BON_ELE', 100, 'Kierunek Kariera - Emisja numer 1 Bonu Elektronicznego', 'Kierunek Kariera - Emisja numer 1 Bonu Elektronicznego', '2016-11-01', 100);
insert into APP_PBE.PBE_PRODUCT_EMISSIONS (ID, PRD_ID, GRANT_PROGRAM_ID, NAME, DESCRIPTION, EMISSION_DATE, QUANTITY)
values ('KK_BON_ELE_EMIS_02', 'KK_BON_ELE', 100, 'Kierunek Kariera - Emisja numer 2 Bonu Elektronicznego', 'Kierunek Kariera - Emisja numer 2 Bonu Elektronicznego', '2016-11-02', 100);


insert into APP_PBE.PBE_PRODUCT_INSTANCES (PRD_ID, NUM, STATUS_ID, EXPIRY_DATE, PRINT_NUM, CRC, PRODUCT_EMISSION_ID, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
select 'KK_BON_ELE', rownum, 'EMITTED', null, null, null,  'KK_BON_ELE_EMIS_01', 1, 'EAGLE', SYSDATE, 'EAGLE', SYSDATE
from DUAL CONNECT BY ROWNUM <= 100;
insert into APP_PBE.PBE_PRODUCT_INSTANCES (PRD_ID, NUM, STATUS_ID, EXPIRY_DATE, PRINT_NUM, CRC, PRODUCT_EMISSION_ID, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
  select 'KK_BON_ELE', rownum + 100, 'EMITTED', null, null, null,  'KK_BON_ELE_EMIS_02', 1, 'EAGLE', SYSDATE, 'EAGLE', SYSDATE
  from DUAL CONNECT BY ROWNUM <= 2000;

insert into APP_PBE.PBE_PRODUCT_INSTANCE_E (ID, PRD_ID, NUM, TYPE_ID, SOURCE_ID, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
  select rownum, 'KK_BON_ELE', NUM, 'EMISSION', PRODUCT_EMISSION_ID, 1, 'EAGLE', SYSDATE, 'EAGLE', SYSDATE
  from  APP_PBE.PBE_PRODUCT_INSTANCES;





insert into APP_PBE.PBE_PRODUCT_INSTANCE_POOLS (ID, START_DATE, EXPIRY_DATE, All_NUM, AVAILABLE_NUM, RESERVED_NUM, USED_NUM, REMBURS_NUM, EXPIRED_NUM,
INDIVIDUAL_ID, ORDER_ID, PRD_ID, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
values (2, to_date('16/09/26','RR/MM/DD'), to_date('17/09/26','RR/MM/DD'), 60, 50, 10, 0, 0, 0, 2, 1270, 'KK_BON_ELE', 1, 'EAGLE', SYSDATE, 'EAGLE', SYSDATE);
insert into APP_PBE.PBE_PRODUCT_INSTANCE_POOLS (ID, START_DATE, EXPIRY_DATE, All_NUM, AVAILABLE_NUM, RESERVED_NUM, USED_NUM, REMBURS_NUM, EXPIRED_NUM,
INDIVIDUAL_ID, ORDER_ID, PRD_ID, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
values (3, to_date('16/09/26','RR/MM/DD'), to_date('18/09/26','RR/MM/DD'), 80, 80, 0, 0, 0, 0, 2, 1271, 'KK_BON_ELE', 1, 'EAGLE', SYSDATE, 'EAGLE', SYSDATE);
insert into APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E (ID, PRODUCT_INSTANCE_POOL_ID, TYPE_ID, SOURCE_ID, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
values (22, 2, 'ASSIGNMENT', null, 1, 'EAGLE', SYSDATE, 'EAGLE', SYSDATE);
insert into APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E (ID, PRODUCT_INSTANCE_POOL_ID, TYPE_ID, SOURCE_ID, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
values (33, 3, 'ASSIGNMENT', null, 1, 'EAGLE', SYSDATE, 'EAGLE', SYSDATE);

insert into APP_PBE.TI_TRAINING_INSTANCES (ID, TRAINING_ID, INDIVIDUAL_ID, GRANT_PROGRAM_ID, STATUS_ID, ASSIGNED_NUM, REGISTER_DATE, REIMBURSMENT_PIN, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
values (1, 1, 2, 100, 'RES', 10, SYSDATE, null, 1, 'EAGLE', SYSDATE, 'EAGLE', SYSDATE);
insert into APP_PBE.TI_TRAINING_INSTANCES (ID, TRAINING_ID, INDIVIDUAL_ID, GRANT_PROGRAM_ID, STATUS_ID, ASSIGNED_NUM, REGISTER_DATE, REIMBURSMENT_PIN, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
values (2, 1, 2, 100, 'DONE', 20, SYSDATE, null, 1, 'EAGLE', SYSDATE, 'EAGLE', SYSDATE);
insert into APP_PBE.TI_TRAINING_INSTANCES (ID, TRAINING_ID, INDIVIDUAL_ID, GRANT_PROGRAM_ID, STATUS_ID, ASSIGNED_NUM, REGISTER_DATE, REIMBURSMENT_PIN, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
values (3, 1, 2, 100, 'REIMB', 30, SYSDATE, null, 1, 'EAGLE', SYSDATE, 'EAGLE', SYSDATE);

insert into APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES (ID, TRAINING_INSTANCE_ID,	PRODUCT_INSTANCE_POOL_ID, ASSIGNED_NUM, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
values (10, 1, 2, 10, 1, 'EAGLE', SYSDATE, 'EAGLE', SYSDATE);