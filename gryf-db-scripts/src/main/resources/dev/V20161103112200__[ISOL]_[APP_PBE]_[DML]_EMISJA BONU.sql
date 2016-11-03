
Insert into APP_STC.PRODUCT_TYPES (TYPE,NAME,PRD_GROUP,CALCULATING,REMARK,PARENT_TYPE,COMPOSIT,BANK_CODE,ACTIVE,WEIGHT)
values ('30','BON ELEKTRONICZNY','V','I',null,null,'N',null,'Y',0);


insert into APP_PBE.PBE_PRODUCTS (PRD_ID, PRD_TYPE, GRANT_OWNER_ID, NAME, VALUE, EXPIRY_DATE, DESCRIPTION, CREATION_DATE)
values ('KK_BON_ELE', '30', 100, 'Kierunek Kariera - Bon Elektroniczny', 15, null, 'Kierunek Kariera - Bon Elektroniczny', SYSDATE);


insert into APP_PBE.PBE_PRODUCT_EMISSIONS (ID, PRD_ID, GRANT_PROGRAM_ID, NAME, DESCRIPTION, EMISSION_DATE, QUANTITY)
values ('KK_BON_ELE_EMIS', 'KK_BON_ELE', 100, 'Kierunek Kariera - Emisja Bonu Elektronicznego', 'Kierunek Kariera - Emisja Bonu Elektronicznego', SYSDATE, 100);

insert into APP_PBE.PBE_PRODUCT_INSTANCES (PRD_ID, NUM, STATUS_ID, EXPIRY_DATE, PRINT_NUM, CRC, ORDER_ID, PRODUCT_EMISSION_ID, E_REIMBURSMENT_ID, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
select 'KK_BON_ELE', rownum, 'NEW', null, null, null, null, 'KK_BON_ELE_EMIS', null, 1, 'EAGLE', SYSDATE, 'EAGLE', SYSDATE
from DUAL CONNECT BY ROWNUM <= 100;


insert into APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS (ID, PRD_ID, NUM, TYPE, SOURCE_ID, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
select eagle.pk_seq.nextval, 'KK_BON_ELE', NUM, 'INIT', null, 1, 'EAGLE', SYSDATE, 'EAGLE', SYSDATE
from  APP_PBE.PBE_PRODUCT_INSTANCES;