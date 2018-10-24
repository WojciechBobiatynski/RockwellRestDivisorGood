--
--Insert into APP_STC.PRODUCT_TYPES (TYPE,NAME,PRD_GROUP,CALCULATING,REMARK,PARENT_TYPE,COMPOSIT,BANK_CODE,ACTIVE,WEIGHT)
--values ('30','BON SZKOLENIOWY ELEKTRONICZNY','V','I',null,null,'N',null,'Y',0);

-- TODO: Ustali� Jaka ma by� warto�� jednego bonu

insert into ${gryf.schema}.PBE_PRODUCTS (PRD_ID, PRD_TYPE, GRANT_OWNER_ID, NAME, VALUE, EXPIRY_DATE, DESCRIPTION, CREATION_DATE, LAST_INSTANCE_NUM)
values ('KZ_BON_EL', '30', 100, 'Kierunek Kariera Zawodowa - Bon Elektroniczny', 15, null, 'Kierunek Kariera Zawodowa - Bon Elektroniczny', SYSDATE, ${gryf.pbe_product_instances.number});

-- dodane w WZ_CONFIG:
--Insert into ${gryf.schema}.GRANT_OWNER_AID_PRODUCTS
--   (ID, GRANT_OWNER_ID, NAME, DESCRIPTION)
-- Values
--   ('BONELEKZ', 100, 'BONY KIERUNEK KARIERA ZAWODOWA', 'Bony dla WUP Krakow - Kierunek Kariera Zawodowa');
   
-- dodane w WZ_CONFIG:
--Insert into ${gryf.schema}.GRANT_PROGRAM_PRODUCTS
--   (ID, GRANT_PROGRAM_ID, PRD_ID, DATE_FROM, DATE_TO,
--    AID_PRODUCT_ID, PBE_PRD_ID)
-- Values
--   (3, 101, NULL, NULL, NULL,
--    'BONELEKZ', 'KZ_BON_EL');

insert into ${gryf.schema}.PBE_PRODUCT_EMISSIONS (ID, PRD_ID, GRANT_PROGRAM_ID, NAME, DESCRIPTION, EMISSION_DATE, QUANTITY)
values ('3', 'KZ_BON_EL', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'Kierunek Kariera Zawodowa - Pierwsza emisja', 'Kierunek Kariera - Pierwsza emisja ${gryf.pbe_product_instances.number} bon�w', SYSDATE, ${gryf.pbe_product_instances.number});



insert into ${gryf.schema}.PBE_PRODUCT_INSTANCES (PRD_ID, NUM, STATUS_ID, EXPIRY_DATE, PRINT_NUM, CRC, PRODUCT_EMISSION_ID, VERSION, CREATED_USER, CREATED_TIMESTAMP, MODIFIED_USER, MODIFIED_TIMESTAMP)
WITH num ( n ) AS (
    SELECT
        1
    FROM
        dual
    UNION ALL
    SELECT
        n + 1
    FROM
        num
    WHERE
        n <= ${gryf.pbe_product_instances.number}
)
SELECT
    'KZ_BON_EL',
    n,
    'EMI',
    NULL,
    NULL,
    NULL,
    '1',
    3,
    user,
    SYSDATE,
    user,
    SYSDATE
FROM
    num;


insert into ${gryf.schema}.PBE_PRODUCT_INSTANCE_E (ID, PRD_ID, NUM, TYPE_ID, SOURCE_ID, CREATED_USER, CREATED_TIMESTAMP)
  select ${eagle.schema}.pk_seq.nextval, 'KZ_BON_EL', NUM, 'EMISSION', PRODUCT_EMISSION_ID, USER, SYSDATE
  from  ${gryf.schema}.PBE_PRODUCT_INSTANCES where PRODUCT_EMISSION_ID = '3';


