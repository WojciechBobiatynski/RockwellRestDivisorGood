-- Nie jest potrzebne (BY MGU)
--Insert into ${gryf.schema}.GRANT_OWNERS (ID,NAME,EMAIL_ADDRESSES_GRANT_APP_INFO) values (100,'Sodexo - Kierunek Kariera','marcel.golunski@sodexo.com');

Insert into ${gryf.schema}.GRANT_PROGRAMS
   (ID, GRANT_OWNER_ID, PROGRAM_NAME, START_DATE, END_DATE,
    PROGRAM_CODE, PROGRAM_CODE_TURN)
 Values
   (GRANT_PROGRAMS_SEQ.NEXTVAL, 100, 'Kierunek Kariera Zawodowa', TO_DATE('07/11/2018 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL,
    '${program.code.wupkkz}', '400002');


Insert into ${gryf.schema}.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'D_FOR_CORR', '5', NULL,
    NULL);
Insert into ${gryf.schema}.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'D_FOR_REIM', '10', NULL,
    NULL);
Insert into ${gryf.schema}.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'EMAIL_FROM', 'system.bonow.szkoleniowych@bony.sodexo.pl', NULL,
    NULL);
Insert into ${gryf.schema}.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'EMAIL_RT', 'tbok.kk@sodexo.com', NULL,
    NULL);
Insert into ${gryf.schema}.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'EX_POL_DAY', '0', TO_DATE('08/10/2018 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
    NULL);
Insert into ${gryf.schema}.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'AUTO_REIMB', 'N', NULL,
    NULL);
Insert into ${gryf.schema}.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'OWN_CONT_P', '13', NULL,
    NULL);
Insert into ${gryf.schema}.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'SXO_NRB_RF', '05160010710003011205015003', NULL,
    NULL);
Insert into ${gryf.schema}.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'E_DAY_LIM', '60', NULL,
    NULL);
Insert into ${gryf.schema}.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'ENROLL_GAP', '0', NULL,
    NULL);


Insert into APP_PBE.GRANT_PROGRAM_PARAM_TYPES
   (ID, NAME, DESCRIPRTION)
 Values
   ('CTR_LST_ID', 'Ostatni użyty id kontraktu', 'Id kontraktu wykorzystywane do generacji APP_PBE.ACCOUNT_CONTRACT_PAIRS. Numer kontraktu ma struturę: <CTR_PREFIX>/<CTR_LAST_ID><CTR_PSTFIX>/[12] np.WKK/123Z/1');

Insert into APP_PBE.GRANT_PROGRAM_PARAMS (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM, DATE_TO) Values  (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'CTR_LST_ID', '0', NULL, NULL);

Insert into APP_PBE.GRANT_PROGRAM_PARAM_TYPES
   (ID, NAME, DESCRIPRTION)
 Values
   ('CTR_PSTFIX', 'Postfix w numerze kontraktu', 'Postfiks zawarty w numerze kontraktu. Numer kontraktu ma struturę: <CTR_PREFIX>/<CTR_LAST_ID><CTR_PSTFIX>/[12] np.WKK/123Z/1');

Insert into APP_PBE.GRANT_PROGRAM_PARAMS (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM, DATE_TO) Values  (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'CTR_PSTFIX', 'Z', NULL, NULL);

Insert into APP_PBE.GRANT_PROGRAM_PARAM_TYPES
   (ID, NAME, DESCRIPRTION)
 Values
   ('CTR_PREFIX', 'Prefix w numerze kontraktu', 'Prefix zawarty w numerze kontraktu. Numer kontraktu ma struturę: <CTR_PREFIX>/<CTR_LAST_ID><CTR_PSTFIX>/[12] np.WKK/123Z/1');

Insert into APP_PBE.GRANT_PROGRAM_PARAMS (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM, DATE_TO) Values  (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'CTR_PREFIX', 'WZ', NULL, NULL);

Insert into APP_PBE.GRANT_PROGRAM_PARAM_TYPES
   (ID, NAME, DESCRIPRTION)
 Values
   ('CODE_LST_N', 'Ostatni numer CODE', 'Używany przy generacji APP_PBE.ACCOUNT_CONTRACT_PAIRS.ACCOUNT_PAYMENT. Numer konta użyty w dalszej kolejności do ustalenia CODE  dla APP_PBE.INDIVIDUALS');

Insert into APP_PBE.GRANT_PROGRAM_PARAMS (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM, DATE_TO) Values  (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'CODE_LST_N', '0', NULL, NULL);

Insert into APP_PBE.GRANT_PROGRAM_PARAM_TYPES
   (ID, NAME, DESCRIPRTION)
 Values
   ('CODE_PRFX', 'Przedrostek CODE', 'Używany przy generacji APP_PBE.ACCOUNT_CONTRACT_PAIRS.ACCOUNT_PAYMENT. Numer konta użyty w dalszej kolejności do ustalenia CODE  dla APP_PBE.INDIVIDUALS');

Insert into APP_PBE.GRANT_PROGRAM_PARAMS (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM, DATE_TO) Values  (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'CODE_PRFX', '601', NULL, NULL);



Insert into ${gryf.schema}.GRANT_PROGRAM_LIMITS
   (ID, GRANT_PROGRAM_ID, ENTERPRISE_SIZE_ID, LIMIT_TYPE, LIMIT_VALUE,
    DATE_FROM, DATE_TO)
 Values
   (10, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'SELF', 'ORDVOULIM', 337,
    NULL, NULL);

Insert into ${gryf.schema}.GRANT_PROGRAM_LIMITS
   (ID, GRANT_PROGRAM_ID, ENTERPRISE_SIZE_ID, LIMIT_TYPE, LIMIT_VALUE,
    DATE_FROM, DATE_TO)
 Values
   (11, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'SELF', 'ORDNUMLIM', 2,
    NULL, NULL);


Insert into ${gryf.schema}.ORDER_FLOWS (ID,NAME,INITIAL_STATUS_ID) values (101,'Przepyw zamówienia - Kierunek Kariera Zawodowa','NEWKK');

Insert into ${gryf.schema}.ORDER_FLOW_STATUS_PROPERTIES (ID, ORDER_FLOW_ID, STATUS_ID, FINAL_STATUS, SUCCESS_STATUS) values (5, 101, 'CANCELLDKK', 'Y', 'N');

Insert into ${gryf.schema}.ORDER_FLOWS_FOR_GR_PROGRAMS(ID, GRANT_PROGRAM_ID, ORDER_FLOW_ID, DATE_FROM, DATE_TO) values (2, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 101, null, null);

Insert into ${gryf.schema}.ORDER_FLOW_ALLOWED_DELAYS
   (ID, ORDER_FLOW_ID, DELAY_TYPE_ID, DELAY_VALUE, DELAY_DAYS_TYPE,
    REMARKS)
 Values
   (11, 101, 'PAYAFORD', 14, 'B',
    'Dozwolony czas na płatność po podpisaniu umowy - 14 dni robczych od Daty zamówienia');

Insert into ${gryf.schema}.ORDER_FLOW_ALLOWED_DELAYS
   (ID, ORDER_FLOW_ID, DELAY_TYPE_ID, DELAY_VALUE, DELAY_DAYS_TYPE,
    REMARKS)
 Values
   (12, 101, 'PAYAFSIGN', 14, 'B',
    'Dozwolony czas na płatność po podpisaniu umowy - 14 dni robczych');


Insert into ${gryf.schema}.ORDER_FLOW_STATUS_TRANSITIONS
   (ORDER_FLOW_ID, STATUS_ID, ACTION_ID, ACTION_NAME, ACTION_DESCRIPTION,
    ACTION_CLASS, NEXT_STATUS_ID, AUG_ID_REQUIRED, AUTOMATIC)
 Values
   (101, 'NEWKK', 10012, 'Rejestruj wpłatę', 'Akcja rejestrująca wpatę wkadu wasnego',
    'careerDirectionRegisterPayment', 'PAIDKK', 'GRF_PBE_ORDERS_CONFIRM_PAYMENT', 'Y');
Insert into ${gryf.schema}.ORDER_FLOW_STATUS_TRANSITIONS
   (ORDER_FLOW_ID, STATUS_ID, ACTION_ID, ACTION_NAME, ACTION_DESCRIPTION,
    ACTION_CLASS, NEXT_STATUS_ID, AUG_ID_REQUIRED, AUTOMATIC)
 Values
   (101, 'PAIDKK', 10023, 'Wydaj bony', 'Akcja wydaje bony elektroniczne',
    'careerDirectionCreateProducyInstancePool', 'DOCGENKK', 'GRF_PBE_ORDERS_CREATE_PRODUCTS', 'N');
Insert into ${gryf.schema}.ORDER_FLOW_STATUS_TRANSITIONS
   (ORDER_FLOW_ID, STATUS_ID, ACTION_ID, ACTION_NAME, ACTION_DESCRIPTION,
    ACTION_CLASS, NEXT_STATUS_ID, AUG_ID_REQUIRED, AUTOMATIC)
 Values
   (101, 'DOCGENKK', 10034, 'Drukuj dokumenty', 'Akcja generuje niezbędne dokumenty księgowe',
    'careerDirectionGenerateDocuments', 'PROCESSDKK', 'GRF_PBE_ORDERS_PRINT_DOC', 'N');
Insert into ${gryf.schema}.ORDER_FLOW_STATUS_TRANSITIONS
   (ORDER_FLOW_ID, STATUS_ID, ACTION_ID, ACTION_NAME, ACTION_DESCRIPTION,
    ACTION_CLASS, NEXT_STATUS_ID, AUG_ID_REQUIRED, AUTOMATIC)
 Values
   (101, 'PROCESSDKK', 10045, 'Wyślij zamówienie', 'Akcja wysyła maila do użytkownika z niezbednymi danymi',
    'careerDirectionSendOrder', 'SENTKK', 'GRF_PBE_ORDERS_SEND', 'N');
Insert into ${gryf.schema}.ORDER_FLOW_STATUS_TRANSITIONS
   (ORDER_FLOW_ID, STATUS_ID, ACTION_ID, ACTION_NAME, ACTION_DESCRIPTION,
    ACTION_CLASS, NEXT_STATUS_ID, AUG_ID_REQUIRED, AUTOMATIC)
 Values
   (101, 'NEWKK', 10015, 'Anuluj Zamówienie', 'Akcja anuluje zamówienie',
    NULL, 'CANCELLDKK', 'GRF_PBE_ORDERS_CANCEL', 'N');
Insert into ${gryf.schema}.ORDER_FLOW_STATUS_TRANSITIONS
   (ORDER_FLOW_ID, STATUS_ID, ACTION_ID, ACTION_NAME, ACTION_DESCRIPTION,
    ACTION_CLASS, NEXT_STATUS_ID, AUG_ID_REQUIRED, AUTOMATIC)
 Values
   (101, 'PAIDKK', 10025, 'Anuluj Zamówienie', 'Akcja anuluje zamówienie',
    NULL, 'CANCELLDKK', 'GRF_PBE_ORDERS_CANCEL', 'N');


Insert into ${gryf.schema}.GRANT_OWNER_AID_PRODUCTS
   (ID, GRANT_OWNER_ID, NAME, DESCRIPTION)
 Values
   ('BONELEKZ', 100, 'BONY KIERUNEK KARIERA ZAWODOWA', 'Bony dla WUP Krakow - Kierunek Kariera Zawodowa');

Insert into ${gryf.schema}.GRANT_PROGRAM_PRODUCTS
   (ID, GRANT_PROGRAM_ID, PRD_ID, DATE_FROM, DATE_TO,
    AID_PRODUCT_ID, PBE_PRD_ID)
 Values
   (3, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), NULL, NULL, NULL,
    'BONELEKZ', 'KZ_BON_EL');


Insert into ${gryf.schema}.GRANT_PROGRAM_ATTACH_REQ
   (GRANT_PROGRAM_ID, ATTACH_TYPE, DATE_FROM, DATE_TO)
 Values
   ((SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'INVOICE', TO_DATE('04/10/2018 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL);
Insert into ${gryf.schema}.GRANT_PROGRAM_ATTACH_REQ
   (GRANT_PROGRAM_ID, ATTACH_TYPE, DATE_FROM, DATE_TO)
 Values
   ((SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'ATTEST', TO_DATE('04/10/2018 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL);
Insert into ${gryf.schema}.GRANT_PROGRAM_ATTACH_REQ
   (GRANT_PROGRAM_ID, ATTACH_TYPE, DATE_FROM, DATE_TO)
 Values
   ((SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'CERT', TO_DATE('04/10/2018 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL);


-- Definicja Katalogu:
--- Definicja katalogu: ${gryf.schema}.TI_TRAINING_CATEGORY_CAT

Insert into ${gryf.schema}.TI_TRAINING_CATEGORY_CAT
   (ID, NAME, ORDINAL)
 Values
   ('CAT_KZ', 'Katalog szkoleń - Kierunek Kariera Zawodowa', 4);


Insert into ${gryf.schema}.TI_TRAINING_CATEGORY_CAT_GR_PR
   (ID, CATALOG_ID, GRANT_PROGRAM_ID, DATE_FROM, DATE_TO)
 Values
   (2, 'CAT_KZ', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), NULL, NULL);

--Insert into ${gryf.schema}.TI_TRAINING_CATEGORY_CAT
--   (ID, NAME, ORDINAL)
-- Values
--   ('CAT_KZ', 'Katalog szkoleń - Kierunek Kariera', 4);

--1.  Szkolenia rozliczane 1h  ( 45 minut) - max 3 bony

Insert into ${gryf.schema}.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('ADM', 'Administracyjno-usługowa', 1, NULL, 'CAT_KZ');
Insert into ${gryf.schema}.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('BUD', 'Budowlana', 2, NULL, 'CAT_KZ');
Insert into ${gryf.schema}.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('ELE', 'Elektryczno-elektroniczna', 3, NULL, 'CAT_KZ');
Insert into ${gryf.schema}.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('MECH','Mechaniczna i górniczo-hutnicza', 4, NULL, 'CAT_KZ');
Insert into ${gryf.schema}.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('ROL', 'Rolniczo-leśna z ochroną środowiska', 5, NULL, 'CAT_KZ');
Insert into ${gryf.schema}.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('TUR', 'Turystyczno-gastronomiczna', 6, NULL, 'CAT_KZ');
Insert into ${gryf.schema}.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('MED', 'Medyczno-społeczna', 7, NULL, 'CAT_KZ');
Insert into ${gryf.schema}.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('ART', 'Artystyczna ', 8, NULL, 'CAT_KZ');


Insert into ${gryf.schema}.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (7,  'ADM',  (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 3, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);
Insert into ${gryf.schema}.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (8,  'BUD',  (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 3, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);
Insert into ${gryf.schema}.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (9,  'ELE',  (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 3, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);
Insert into ${gryf.schema}.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (10, 'MECH', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 3, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);
Insert into ${gryf.schema}.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (11, 'ROL',  (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 3, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);
Insert into ${gryf.schema}.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (12, 'TUR',  (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 3, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);
Insert into ${gryf.schema}.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (13, 'MED',  (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 3, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);
Insert into ${gryf.schema}.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (14, 'ART',  (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 3, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);



--2.  Szkolenia rozliczane 1h - max 2 bony

Insert into ${gryf.schema}.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('SPD', 'Studia podyplomowe', 1, NULL, 'CAT_KZ');

Insert into ${gryf.schema}.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (16, 'SPD',  (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 2, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);


--3.  Egzamin rozliczane max 80 bonów (1200 zł) EGZKZ

Insert into ${gryf.schema}.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('EGZKZ', 'Egzamin', 1, NULL, 'CAT_KZ');

Insert into ${gryf.schema}.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (17, 'EGZKZ',  (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), NULL, 80, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);


-- parametry programu
