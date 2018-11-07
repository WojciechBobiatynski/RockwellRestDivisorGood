--------------- ---------------------- ------------------------
--------------- MODYFIKACJA TABELI ${gryf.schema}.CONTRACT_TRAINING_CATEG_AUDIT ------------------------
--------------- ---------------------- ------------------------


CREATE TABLE ${gryf.schema}.BCP_PBE_CN_TR_CA_A AS SELECT * FROM ${gryf.schema}.CONTRACT_TRAINING_CATEG_AUDIT;

ALTER TABLE ${gryf.schema}.CONTRACT_TRAINING_CATEG_AUDIT ADD CONTRACT_ID_COPY NUMBER;

UPDATE ${gryf.schema}.CONTRACT_TRAINING_CATEG_AUDIT SET CONTRACT_ID_COPY = CONTRACT_ID;



ALTER TABLE ${gryf.schema}.CONTRACT_TRAINING_CATEG_AUDIT MODIFY CONTRACT_ID NULL;

UPDATE ${gryf.schema}.CONTRACT_TRAINING_CATEG_AUDIT SET CONTRACT_ID = NULL;



ALTER TABLE ${gryf.schema}.CONTRACT_TRAINING_CATEG_AUDIT MODIFY CONTRACT_ID VARCHAR2(100);

UPDATE ${gryf.schema}.CONTRACT_TRAINING_CATEG_AUDIT SET CONTRACT_ID = CONTRACT_ID_COPY;



ALTER TABLE ${gryf.schema}.CONTRACT_TRAINING_CATEG_AUDIT MODIFY CONTRACT_ID NOT NULL;

CREATE TABLE ${gryf.schema}.BCP_PBE_CN_TR_CA_A2 AS SELECT * FROM ${gryf.schema}.CONTRACT_TRAINING_CATEG_AUDIT;

ALTER TABLE ${gryf.schema}.CONTRACT_TRAINING_CATEG_AUDIT DROP COLUMN CONTRACT_ID_COPY;

--------------- ---------------------- ------------------------
--------------- MODYFIKACJA TABELI ${gryf.schema}.CONTRACT_TRAINING_CATEGORIES ------------------------
--------------- ---------------------- ------------------------
CREATE TABLE ${gryf.schema}.BCP_PBE_CN_TR_CA AS SELECT * FROM ${gryf.schema}.CONTRACT_TRAINING_CATEGORIES;

ALTER TABLE ${gryf.schema}.CONTRACT_TRAINING_CATEGORIES DROP CONSTRAINT CON_TRA_CAT_PK;

DROP INDEX ${gryf.schema}.CON_TRA_CAT_PK;

ALTER TRIGGER ${gryf.schema}.TRG_CONTRACT_TRAINING_CATE_AUD DISABLE; 

ALTER TABLE ${gryf.schema}.CONTRACT_TRAINING_CATEGORIES ADD CONTRACT_ID_COPY NUMBER;

UPDATE ${gryf.schema}.CONTRACT_TRAINING_CATEGORIES SET CONTRACT_ID_COPY = CONTRACT_ID;



ALTER TABLE ${gryf.schema}.CONTRACT_TRAINING_CATEGORIES DROP CONSTRAINT CON_TRA_CAT_CON_FK;

ALTER TABLE ${gryf.schema}.CONTRACT_TRAINING_CATEGORIES MODIFY CONTRACT_ID NULL;

UPDATE ${gryf.schema}.CONTRACT_TRAINING_CATEGORIES SET CONTRACT_ID = NULL;

--ORA-00001: naruszono więzy unikatowe (${gryf.schema}.CON_TRA_CAT_PK)



ALTER TABLE ${gryf.schema}.CONTRACT_TRAINING_CATEGORIES MODIFY CONTRACT_ID VARCHAR2(100);

UPDATE ${gryf.schema}.CONTRACT_TRAINING_CATEGORIES SET CONTRACT_ID = CONTRACT_ID_COPY;



ALTER TABLE ${gryf.schema}.CONTRACT_TRAINING_CATEGORIES MODIFY CONTRACT_ID NOT NULL;

CREATE TABLE ${gryf.schema}.BCP_PBE_CN_TR_CA2 AS SELECT * FROM ${gryf.schema}.CONTRACT_TRAINING_CATEGORIES;

ALTER TABLE ${gryf.schema}.CONTRACT_TRAINING_CATEGORIES DROP COLUMN CONTRACT_ID_COPY;

ALTER TRIGGER ${gryf.schema}.TRG_CONTRACT_TRAINING_CATE_AUD DISABLE;

--------------- ---------------------- ------------------------
--------------- MODYFIKACJA TABELI ${gryf.schema}.IMPORT_DATA_ROWS ------------------------
--------------- ---------------------- ------------------------

CREATE TABLE ${gryf.schema}.BCP_IMPORT_DATA_ROWS AS SELECT * FROM ${gryf.schema}.IMPORT_DATA_ROWS;

ALTER TABLE ${gryf.schema}.IMPORT_DATA_ROWS ADD CONTRACT_ID_COPY NUMBER;

UPDATE ${gryf.schema}.IMPORT_DATA_ROWS SET CONTRACT_ID_COPY = CONTRACT_ID;



ALTER TABLE ${gryf.schema}.IMPORT_DATA_ROWS DROP CONSTRAINT IMP_DAT_ROW_CON_FK;

--ALTER TABLE ${gryf.schema}.IMPORT_DATA_ROWS MODIFY CONTRACT_ID NULL;

UPDATE ${gryf.schema}.IMPORT_DATA_ROWS SET CONTRACT_ID = NULL;



ALTER TABLE ${gryf.schema}.IMPORT_DATA_ROWS MODIFY CONTRACT_ID VARCHAR2(100);

UPDATE ${gryf.schema}.IMPORT_DATA_ROWS SET CONTRACT_ID = CONTRACT_ID_COPY;



--ALTER TABLE ${gryf.schema}.IMPORT_DATA_ROWS MODIFY CONTRACT_ID NOT NULL;

CREATE TABLE ${gryf.schema}.BCP_IMPORT_DATA_ROWS2 AS SELECT * FROM ${gryf.schema}.IMPORT_DATA_ROWS;

ALTER TABLE ${gryf.schema}.IMPORT_DATA_ROWS DROP COLUMN CONTRACT_ID_COPY;

--------------- ---------------------- ------------------------
--------------- MODYFIKACJA TABELI ${gryf.schema}.ORDERS_AUDIT ------------------------
--------------- ---------------------- ------------------------
CREATE TABLE ${gryf.schema}.BCP_PBE_ORDERS_A AS SELECT * FROM ${gryf.schema}.ORDERS_AUDIT;

ALTER TABLE ${gryf.schema}.ORDERS_AUDIT ADD CONTRACT_ID_COPY NUMBER;

UPDATE ${gryf.schema}.ORDERS_AUDIT SET CONTRACT_ID_COPY = CONTRACT_ID;



--ALTER TABLE ${gryf.schema}.ORDERS_AUDIT MODIFY CONTRACT_ID NULL;

UPDATE ${gryf.schema}.ORDERS_AUDIT SET CONTRACT_ID = NULL;



ALTER TABLE ${gryf.schema}.ORDERS_AUDIT MODIFY CONTRACT_ID VARCHAR2(100);

UPDATE ${gryf.schema}.ORDERS_AUDIT SET CONTRACT_ID = CONTRACT_ID_COPY;



--ALTER TABLE ${gryf.schema}.ORDERS_AUDIT MODIFY CONTRACT_ID NOT NULL;

CREATE TABLE ${gryf.schema}.BCP_PBE_ORDERS_A2 AS SELECT * FROM ${gryf.schema}.ORDERS_AUDIT;

ALTER TABLE ${gryf.schema}.ORDERS_AUDIT DROP COLUMN CONTRACT_ID_COPY;

--------------- ---------------------- ------------------------
--------------- MODYFIKACJA TABELI ${gryf.schema}.ORDERS ------------------------
--------------- ---------------------- ------------------------

CREATE TABLE ${gryf.schema}.BCP_PBE_ORDERS AS SELECT * FROM ${gryf.schema}.ORDERS;

ALTER TABLE ${gryf.schema}.ORDERS ADD CONTRACT_ID_COPY NUMBER;

UPDATE ${gryf.schema}.ORDERS SET CONTRACT_ID_COPY = CONTRACT_ID;



ALTER TABLE ${gryf.schema}.ORDERS DROP CONSTRAINT ORD_CONT_FK;

--ALTER TABLE ${gryf.schema}.ORDERS MODIFY CONTRACT_ID NULL;

UPDATE ${gryf.schema}.ORDERS SET CONTRACT_ID = NULL;



ALTER TABLE ${gryf.schema}.ORDERS MODIFY CONTRACT_ID VARCHAR2(100);

UPDATE ${gryf.schema}.ORDERS SET CONTRACT_ID = CONTRACT_ID_COPY;



--ALTER TABLE ${gryf.schema}.ORDERS MODIFY CONTRACT_ID NOT NULL;

CREATE TABLE ${gryf.schema}.BCP_PBE_ORDERS2 AS SELECT * FROM ${gryf.schema}.ORDERS;

ALTER TABLE ${gryf.schema}.ORDERS DROP COLUMN CONTRACT_ID_COPY;

--------------- ---------------------- ------------------------
--------------- MODYFIKACJA TABELI ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS_AUDIT ------------------------
--------------- ---------------------- ------------------------

CREATE TABLE ${gryf.schema}.BCP_PBE_CC_CON_PAIRS_A AS SELECT * FROM ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS_AUDIT;

ALTER TABLE ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS_AUDIT ADD CONTRACT_ID_COPY NUMBER;

UPDATE ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS_AUDIT SET CONTRACT_ID_COPY = CONTRACT_ID;



ALTER TABLE ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS_AUDIT MODIFY CONTRACT_ID NULL;

UPDATE ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS_AUDIT SET CONTRACT_ID = NULL;



ALTER TABLE ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS_AUDIT MODIFY CONTRACT_ID VARCHAR2(100);

UPDATE ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS_AUDIT SET CONTRACT_ID = CONTRACT_ID_COPY;



ALTER TABLE ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS_AUDIT MODIFY CONTRACT_ID NOT NULL;

CREATE TABLE ${gryf.schema}.BCP_PBE_CC_CON_PAIRS_A2 AS SELECT * FROM ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS_AUDIT;

ALTER TABLE ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS_AUDIT DROP COLUMN CONTRACT_ID_COPY;

--------------- ---------------------- ------------------------
--------------- MODYFIKACJA TABELI ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS ------------------------
--------------- ---------------------- ------------------------

CREATE TABLE ${gryf.schema}.BCP_PBE_CC_CON_PAIRS AS SELECT * FROM ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS;

ALTER TABLE ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS ADD CONTRACT_ID_COPY NUMBER;

UPDATE ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS SET CONTRACT_ID_COPY = CONTRACT_ID;



ALTER TABLE ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS MODIFY CONTRACT_ID NULL;

UPDATE ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS SET CONTRACT_ID = NULL;



ALTER TABLE ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS MODIFY CONTRACT_ID VARCHAR2(100);

ALTER TRIGGER ${gryf.schema}.TRG_ACCOUNT_CONTRACT_PAIRS_AUD DISABLE;

UPDATE ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS SET CONTRACT_ID = CONTRACT_ID_COPY;



ALTER TABLE ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS MODIFY CONTRACT_ID NOT NULL;

CREATE TABLE ${gryf.schema}.BCP_PBE_CC_CON_PAIRS2 AS SELECT * FROM ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS;

ALTER TABLE ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS DROP COLUMN CONTRACT_ID_COPY;

ALTER TRIGGER ${gryf.schema}.TRG_ACCOUNT_CONTRACT_PAIRS_AUD ENABLE;

--------------- ---------------------- ------------------------
--------------- MODYFIKACJA TABELI ${gryf.schema}.CONTRACTS_AUDIT ------------------------
--------------- ---------------------- ------------------------

CREATE TABLE ${gryf.schema}.BCP_PBE_CONTRACTS_A AS SELECT * FROM ${gryf.schema}.CONTRACTS_AUDIT;

ALTER TABLE ${gryf.schema}.CONTRACTS_AUDIT ADD CONTRACT_ID_COPY NUMBER;

UPDATE ${gryf.schema}.CONTRACTS_AUDIT SET CONTRACT_ID_COPY = ID;



ALTER TABLE ${gryf.schema}.CONTRACTS_AUDIT MODIFY ID NULL;

UPDATE ${gryf.schema}.CONTRACTS_AUDIT SET ID = NULL;



ALTER TABLE ${gryf.schema}.CONTRACTS_AUDIT MODIFY ID VARCHAR2(100);

UPDATE ${gryf.schema}.CONTRACTS_AUDIT SET ID = CONTRACT_ID_COPY;



ALTER TABLE ${gryf.schema}.CONTRACTS_AUDIT MODIFY ID NOT NULL;

CREATE TABLE ${gryf.schema}.BCP_PBE_CONTRACTS_A2 AS SELECT * FROM ${gryf.schema}.CONTRACTS_AUDIT;

ALTER TABLE ${gryf.schema}.CONTRACTS_AUDIT DROP COLUMN CONTRACT_ID_COPY;

--------------- ---------------------- ------------------------
--------------- MODYFIKACJA TABELI ${gryf.schema}.CONTRACTS ------------------------
--------------- ---------------------- ------------------------

CREATE TABLE ${gryf.schema}.BCP_PBE_CONTRACTS AS SELECT * FROM ${gryf.schema}.CONTRACTS;

ALTER TABLE ${gryf.schema}.CONTRACTS ADD CONTRACT_ID_COPY NUMBER;

UPDATE ${gryf.schema}.CONTRACTS SET CONTRACT_ID_COPY = ID;



ALTER TABLE ${gryf.schema}.CONTRACTS DISABLE CONSTRAINT CONTRACTS_PK;

ALTER TABLE ${gryf.schema}.CONTRACTS MODIFY ID NULL;

UPDATE ${gryf.schema}.CONTRACTS SET ID = NULL;



ALTER TABLE ${gryf.schema}.CONTRACTS MODIFY ID VARCHAR2(100);

ALTER TRIGGER ${gryf.schema}.TRG_CONTRACTS_AUDIT DISABLE;

UPDATE ${gryf.schema}.CONTRACTS SET ID = CONTRACT_ID_COPY;



ALTER TABLE ${gryf.schema}.CONTRACTS MODIFY ID NOT NULL;

CREATE TABLE ${gryf.schema}.BCP_PBE_CONTRACTS2 AS SELECT * FROM ${gryf.schema}.CONTRACTS;

ALTER TABLE ${gryf.schema}.CONTRACTS DROP COLUMN CONTRACT_ID_COPY;

ALTER TRIGGER ${gryf.schema}.TRG_CONTRACTS_AUDIT DISABLE;


--------------- ---------------------- ------------------------
--------------- WŁĄCZENIE CONSTRAINTOW TABELI ${gryf.schema}.CONTRACTS ------------------------
--------------- ---------------------- ------------------------

ALTER TABLE ${gryf.schema}.CONTRACTS ENABLE CONSTRAINT CONTRACTS_PK;

--"ALTER TABLE ${gryf.schema}.ORDERS ADD (
--  CONSTRAINT ORD_CONT_FK 
--  FOREIGN KEY (CONTRACT_ID) 
--  REFERENCES ${gryf.schema}.CONTRACTS (ID)
--  ENABLE VALIDATE
--  );"


--------------- ---------------------- ------------------------
--------------- WŁĄCZENIE CONSTRAINTOW TABELI ${gryf.schema}.CONTRACT_TRAINING_CATEG_AUDIT ------------------------
--------------- ---------------------- ------------------------
--------------- ---------------------- ------------------------
--------------- WŁĄCZENIE CONSTRAINTOW TABELI ${gryf.schema}.CONTRACT_TRAINING_CATEGORIES ------------------------
--------------- ---------------------- ------------------------

ALTER TABLE ${gryf.schema}.CONTRACT_TRAINING_CATEGORIES ADD (
  CONSTRAINT CON_TRA_CAT_CON_FK 
  FOREIGN KEY (CONTRACT_ID) 
  REFERENCES ${gryf.schema}.CONTRACTS (ID)
  );

-- DODANIE CONSTRAINTA PK
ALTER TABLE ${gryf.schema}.CONTRACT_TRAINING_CATEGORIES ADD (
  CONSTRAINT CON_TRA_CAT_PK
  PRIMARY KEY
  (CONTRACT_ID, TRAINING_CATEGORY_ID)
  USING INDEX );
  
--------------- ---------------------- ------------------------
--------------- WŁĄCZENIE CONSTRAINTOW TABELI ${gryf.schema}.IMPORT_DATA_ROWS ------------------------
--------------- ---------------------- ------------------------

ALTER TABLE ${gryf.schema}.IMPORT_DATA_ROWS ADD (
  CONSTRAINT IMP_DAT_ROW_CON_FK 
  FOREIGN KEY (CONTRACT_ID) 
  REFERENCES ${gryf.schema}.CONTRACTS (ID)
);

--------------- ---------------------- ------------------------
--------------- WŁĄCZENIE CONSTRAINTOW TABELI ${gryf.schema}.ORDERS_AUDIT ------------------------
--------------- ---------------------- ------------------------
--------------- ---------------------- ------------------------
--------------- WŁĄCZENIE CONSTRAINTOW TABELI ${gryf.schema}.ORDERS ------------------------
--------------- ---------------------- ------------------------

ALTER TABLE ${gryf.schema}.ORDERS ADD (
  CONSTRAINT ORD_CONT_FK 
  FOREIGN KEY (CONTRACT_ID) 
  REFERENCES ${gryf.schema}.CONTRACTS (ID)
  );

--------------- ---------------------- ------------------------
--------------- WŁĄCZENIE CONSTRAINTOW TABELI ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS_AUDIT ------------------------
--------------- ---------------------- ------------------------
--------------- ---------------------- ------------------------
--------------- WŁĄCZENIE CONSTRAINTOW TABELI ${gryf.schema}.ACCOUNT_CONTRACT_PAIRS ------------------------
--------------- ---------------------- ------------------------
--------------- ---------------------- ------------------------
--------------- WŁĄCZENIE CONSTRAINTOW TABELI ${gryf.schema}.CONTRACTS_AUDIT ------------------------
--------------- ---------------------- ------------------------



