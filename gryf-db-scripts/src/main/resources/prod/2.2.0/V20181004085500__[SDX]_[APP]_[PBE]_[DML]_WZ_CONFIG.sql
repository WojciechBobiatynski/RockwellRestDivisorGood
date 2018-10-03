-- TODO: Ustalić czy jest potrzebne
Insert into APP_PBE.GRANT_OWNERS (ID,NAME,EMAIL_ADDRESSES_GRANT_APP_INFO) values (101,'Sodexo - Kierunek Kariera Zawodowa','bartlomiej.ponichtera@sodexo.com');
Insert into APP_PBE.GRANT_PROGRAMS
   (ID, GRANT_OWNER_ID, PROGRAM_NAME, START_DATE, END_DATE,
    PROGRAM_CODE, PROGRAM_CODE_TURN)
 Values
   (101, 101, 'Kierunek Kariera Zawodowa', TO_DATE('10/04/2018 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL,
    'WUPKKZ', '400001');

-- TODO: 1. Ustalić datę rozpoczęcia,
-- TODO: 2. Ustlić z Fin: PROGRAM_CODE,
-- TODO: 3. Ustlić z Fin: PROGRAM_CODE_TURN



-- TODO: Upewnić się, że nie dodajemy nowych parametrów programu
--Insert into APP_PBE.GRANT_PROGRAM_PARAM_TYPES (ID, NAME, DESCRIPRTION) values ('OWN_CONT_P','Procent wkładu własnego','Procent wkładu własnego jaką użytkownik musi wpłacić przy realizacji danego programu dofinansownaia. Wartoąść parametru musi być liczba bez znaku procent np: 13, 30.33, 12.341');
--Insert into APP_PBE.GRANT_PROGRAM_PARAM_TYPES (ID, NAME, DESCRIPRTION) values ('SXO_NRB_RF','Rachunek bankowy Sxo do wypłat','Rachunek bankowy Sodexo, z którego wypłacane są środki w ramach programu');
--

-- TODO: Ustalić, czy nie zmieniają się wartości parametrów:
--select PPT.NAME, PPT.DESCRIPRTION, PP.*
--from   APP_PBE.GRANT_PROGRAM_PARAMS PP
--     JOIN APP_PBE.GRANT_PROGRAM_PARAM_TYPES PPT ON PPT.ID = PP.PARAM_ID
--WHERE  PP.ID NOT IN (5,13)


Insert into APP_PBE.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (15, 101, 'D_FOR_CORR', '5', NULL,
    NULL);
Insert into APP_PBE.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (16, 101, 'D_FOR_REIM', '10', NULL,
    NULL);
Insert into APP_PBE.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (17, 101, 'EMAIL_FROM', 'system.bonow.szkoleniowych@bony.sodexo.pl', NULL,
    NULL);
Insert into APP_PBE.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (18, 101, 'EMAIL_RT', 'tbok.kk@sodexo.com', NULL,
    NULL);
Insert into APP_PBE.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (19, 101, 'EX_POL_DAY', '0', TO_DATE('08/10/2018 00:00:00', 'MM/DD/YYYY HH24:MI:SS'),
    NULL);
Insert into APP_PBE.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (20, 101, 'AUTO_REIMB', 'N', NULL,
    NULL);
Insert into APP_PBE.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (21, 101, 'OWN_CONT_P', '13', NULL,
    NULL);
Insert into APP_PBE.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (22, 101, 'SXO_NRB_RF', '32160010710003011205015002', NULL,
    NULL);
Insert into APP_PBE.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (23, 101, 'E_DAY_LIM', '35', NULL,
    NULL);
Insert into APP_PBE.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (24, 101, 'ENROLL_GAP', '0', NULL,
    NULL);


Insert into APP_PBE.GRANT_PROGRAM_LIMITS
   (ID, GRANT_PROGRAM_ID, ENTERPRISE_SIZE_ID, LIMIT_TYPE, LIMIT_VALUE,
    DATE_FROM, DATE_TO)
 Values
   (8, 101, 'SELF', 'ORDVOULIM', 180,
    NULL, NULL);
Insert into APP_PBE.GRANT_PROGRAM_LIMITS
   (ID, GRANT_PROGRAM_ID, ENTERPRISE_SIZE_ID, LIMIT_TYPE, LIMIT_VALUE,
    DATE_FROM, DATE_TO)
 Values
   (9, 101, 'SELF', 'ORDNUMLIM', 2,
    NULL, NULL);

-- TODO: Upewnić się że nie dodajemy nowych statusów (WAŻNE: jeśli nie, to zabiera to możliwość konfiguracji: INNE APP_PBE.ORDER_FLOW_ELEMENTS )
--Insert into APP_PBE.ORDER_FLOW_STATUSES (STATUS_ID,STATUS_NAME) values ('NEWKK', 'Nowe');
--Insert into APP_PBE.ORDER_FLOW_STATUSES (STATUS_ID,STATUS_NAME) values ('PAIDKK', 'Opłacone');
--Insert into APP_PBE.ORDER_FLOW_STATUSES (STATUS_ID,STATUS_NAME) values ('DOCGENKK', 'Wygenerowano dokumenty');
--Insert into APP_PBE.ORDER_FLOW_STATUSES (STATUS_ID,STATUS_NAME) values ('PROCESSDKK', 'Zrealizowane');
--Insert into APP_PBE.ORDER_FLOW_STATUSES (STATUS_ID,STATUS_NAME) values ('SENTKK', 'Wysłane');
--Insert into APP_PBE.ORDER_FLOW_STATUSES (STATUS_ID,STATUS_NAME) values ('EKSPORTKK', 'Wyeksportowane');
--Insert into APP_PBE.ORDER_FLOW_STATUSES (STATUS_ID,STATUS_NAME) values ('CANCELLDKK', 'Anulowane');



Insert into APP_PBE.ORDER_FLOWS (ID,NAME,INITIAL_STATUS_ID) values (101,'Przepyw zamówienia - Kierunek Kariera Zawodowa','NEWKK');


Insert into APP_PBE.ORDER_FLOW_STATUS_PROPERTIES (ID, ORDER_FLOW_ID, STATUS_ID, FINAL_STATUS, SUCCESS_STATUS) values (5, 101, 'CANCELLDKK', 'Y', 'N');

Insert into APP_PBE.ORDER_FLOWS_FOR_GR_PROGRAMS(ID, GRANT_PROGRAM_ID, ORDER_FLOW_ID, DATE_FROM, DATE_TO) values (2, 101, 101, null, null);


---- TODO: Upewnić się, ze nie ma innych APP_PBE.ORDER_FLOW_ALLOWED_DELAY_TYPES
--update APP_PBE.ORDER_FLOW_ALLOWED_DELAY_TYPES set NAME = 'Dopuszczalne opóźnienie płatności po umowie', DESCRIPTION = 'Dopuszczalne opóźnienie na dokonanie płatności po podpisaniu umowy' where ID = 'PAYAFSIGN';


-- TODO: Potwierdzić wartości:
--
--select dt.name, DT.DESCRIPTION, d.*
--from   APP_PBE.ORDER_FLOW_ALLOWED_DELAYS d
--       join APP_PBE.ORDER_FLOW_ALLOWED_DELAY_TYPES dt on DT.ID = D.DELAY_TYPE_ID

Insert into APP_PBE.ORDER_FLOW_ALLOWED_DELAYS
   (ID, ORDER_FLOW_ID, DELAY_TYPE_ID, DELAY_VALUE, DELAY_DAYS_TYPE,
    REMARKS)
 Values
   (11, 101, 'PAYAFORD', 14, 'B',
    'Dozwolony czas na płatność po podpisaniu umowy - 14 dni robczych od Daty zamówienia');
Insert into APP_PBE.ORDER_FLOW_ALLOWED_DELAYS
   (ID, ORDER_FLOW_ID, DELAY_TYPE_ID, DELAY_VALUE, DELAY_DAYS_TYPE,
    REMARKS)
 Values
   (12, 101, 'PAYAFSIGN', 14, 'B',
    'Dozwolony czas na płatność po podpisaniu umowy - 14 dni robczych');


--TODO: Ustalić, czy zmieniać ACTION_ID
--TODO: Potwierdzić, że przepływ się nie zmienia
Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS
   (ORDER_FLOW_ID, STATUS_ID, ACTION_ID, ACTION_NAME, ACTION_DESCRIPTION,
    ACTION_CLASS, NEXT_STATUS_ID, AUG_ID_REQUIRED, AUTOMATIC)
 Values
   (101, 'NEWKK', 10012, 'Rejestruj wpłatę', 'Akcja rejestrująca wpatę wkadu wasnego',
    'careerDirectionRegisterPayment', 'PAIDKK', 'GRF_PBE_ORDERS_CONFIRM_PAYMENT', 'Y');
Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS
   (ORDER_FLOW_ID, STATUS_ID, ACTION_ID, ACTION_NAME, ACTION_DESCRIPTION,
    ACTION_CLASS, NEXT_STATUS_ID, AUG_ID_REQUIRED, AUTOMATIC)
 Values
   (101, 'PAIDKK', 10023, 'Wydaj bony', 'Akcja wydaje bony elektroniczne',
    'careerDirectionCreateProducyInstancePool', 'DOCGENKK', 'GRF_PBE_ORDERS_CREATE_PRODUCTS', 'N');
Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS
   (ORDER_FLOW_ID, STATUS_ID, ACTION_ID, ACTION_NAME, ACTION_DESCRIPTION,
    ACTION_CLASS, NEXT_STATUS_ID, AUG_ID_REQUIRED, AUTOMATIC)
 Values
   (101, 'DOCGENKK', 10034, 'Drukuj dokumenty', 'Akcja generuje niezbędne dokumenty księgowe',
    'careerDirectionGenerateDocuments', 'PROCESSDKK', 'GRF_PBE_ORDERS_PRINT_DOC', 'N');
Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS
   (ORDER_FLOW_ID, STATUS_ID, ACTION_ID, ACTION_NAME, ACTION_DESCRIPTION,
    ACTION_CLASS, NEXT_STATUS_ID, AUG_ID_REQUIRED, AUTOMATIC)
 Values
   (101, 'PROCESSDKK', 10045, 'Wyślij zamówienie', 'Akcja wysyła maila do użytkownika z niezbednymi danymi',
    'careerDirectionSendOrder', 'SENTKK', 'GRF_PBE_ORDERS_SEND', 'N');
Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS
   (ORDER_FLOW_ID, STATUS_ID, ACTION_ID, ACTION_NAME, ACTION_DESCRIPTION,
    ACTION_CLASS, NEXT_STATUS_ID, AUG_ID_REQUIRED, AUTOMATIC)
 Values
   (101, 'NEWKK', 10015, 'Anuluj Zamówienie', 'Akcja anuluje zamówienie',
    NULL, 'CANCELLDKK', 'GRF_PBE_ORDERS_CANCEL', 'N');
Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS
   (ORDER_FLOW_ID, STATUS_ID, ACTION_ID, ACTION_NAME, ACTION_DESCRIPTION,
    ACTION_CLASS, NEXT_STATUS_ID, AUG_ID_REQUIRED, AUTOMATIC)
 Values
   (101, 'PAIDKK', 10025, 'Anuluj Zamówienie', 'Akcja anuluje zamówienie',
    NULL, 'CANCELLDKK', 'GRF_PBE_ORDERS_CANCEL', 'N');

-- TODO: Upewnić się, - Jeśli nie dodajemy nowych statusów, to nie dodajemy tych wpisów
--Insert into APP_PBE.ORDER_FLOW_ELEMENT_TYPES (ID,DESCRIPTION,COMPONENT_NAME) values ('C_CONTRA','Element złożony reprezentujący podstawowe dane o umowie. Front: html i JS, Backend: serwisy, DTOsy do obsługi elementu złożonego powoływane są dynamicznie na podstawie nazwy przechowywanej w kolumnie COMPONENT_NAME','ComplexTypeBasicContractInfo');


--TODO: Upewnić się, - Jeśli nie dodajemy nowych statusów, to nie dodajemy tych wpisów
--Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('ORDINF_KK','Informacje o zamówieniu','C_BSORDIFO',null,'Element złożony przetrzymujący podstawowe informacje o zamówieniu');
--


-- TODO: Upewnić się, - Jeśli nie dodajemy nowych statusów, to nie dodajemy tych wpisów
--Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('NEWKK',       'ORDINF_KK',1, null, null,null);

-- TODO: Potwierdzić kod produktu
Insert into APP_PBE.GRANT_OWNER_AID_PRODUCTS
   (ID, GRANT_OWNER_ID, NAME, DESCRIPTION)
 Values
   ('BONELEKZ', 101, 'BONY KIERUNEK KARIERA ZAWODOWA', 'Bony dla WUP Krakow - Kierunek Kariera Zawodowa');

-- TODO: USTALIĆ PBE_PRD_ID - Nie wiadomo, co to oznacza (Poniżej zmieniłem kod???)
Insert into APP_PBE.GRANT_PROGRAM_PRODUCTS
   (ID, GRANT_PROGRAM_ID, PRD_ID, DATE_FROM, DATE_TO,
    AID_PRODUCT_ID, PBE_PRD_ID)
 Values
   (3, 101, NULL, NULL, NULL,
    'BONELEKZ', 'KZ_BON_EL');

-- TODO: Potwierdzić, że nie wymagane są inne załączniki:
--Insert into APP_PBE.ATTACHMENT_TYPES
--   (CODE, NAME, ORDINAL, MAX_BYTES_PER_FILE, MAX_FILES_PER_TYPE)
-- Values
--   ('INVOICE', 'Faktura', 1, 4194304, NULL);

--todo: potwierdzić, że wykorzystywane są te same załączniki */
Insert into APP_PBE.GRANT_PROGRAM_ATTACH_REQ
   (GRANT_PROGRAM_ID, ATTACH_TYPE, DATE_FROM, DATE_TO)
 Values
   (101, 'INVOICE', TO_DATE('04/10/2018 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL);
Insert into APP_PBE.GRANT_PROGRAM_ATTACH_REQ
   (GRANT_PROGRAM_ID, ATTACH_TYPE, DATE_FROM, DATE_TO)
 Values
   (101, 'ATTEST', TO_DATE('04/10/2018 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL);
Insert into APP_PBE.GRANT_PROGRAM_ATTACH_REQ
   (GRANT_PROGRAM_ID, ATTACH_TYPE, DATE_FROM, DATE_TO)
 Values
   (101, 'CERT', TO_DATE('04/10/2018 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), NULL);


-- Definicja Katalogu:
--- Definicja katalogu: APP_PBE.TI_TRAINING_CATEGORY_CAT

Insert into APP_PBE.TI_TRAINING_CATEGORY_CAT
   (ID, NAME, ORDINAL)
 Values
   ('CAT_KZ', 'Katalog szkoleń - Kierunek Kariera Zawodowa', 4);


SET DEFINE OFF;
Insert into APP_PBE.TI_TRAINING_CATEGORY_CAT_GR_PR
   (ID, CATALOG_ID, GRANT_PROGRAM_ID, DATE_FROM, DATE_TO)
 Values
   (2, 'CAT_KZ', 101, NULL, NULL);
COMMIT;

--Insert into APP_PBE.TI_TRAINING_CATEGORY_CAT
--   (ID, NAME, ORDINAL)
-- Values
--   ('CAT_KZ', 'Katalog szkoleń - Kierunek Kariera', 4);

--1.  Szkolenia rozliczane 1h  ( 45 minut) - max 3 bony

Insert into APP_PBE.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('ADM', 'Administracyjno-usługowa', 1, NULL, 'CAT_KZ');
Insert into APP_PBE.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('BUD', 'Budowlana', 2, NULL, 'CAT_KZ');
Insert into APP_PBE.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('ELE', 'Elektryczno-elektroniczna', 3, NULL, 'CAT_KZ');
Insert into APP_PBE.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('MECH','Mechaniczna i górniczo-hutnicza', 4, NULL, 'CAT_KZ');
Insert into APP_PBE.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('ROL', 'Rolniczo-leśna z ochroną środowiska', 5, NULL, 'CAT_KZ');
Insert into APP_PBE.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('TUR', 'Turystyczno-gastronomiczna', 6, NULL, 'CAT_KZ');
Insert into APP_PBE.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('MED', 'Medyczno-społeczna', 7, NULL, 'CAT_KZ');
Insert into APP_PBE.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('ART', 'Artystyczna ', 8, NULL, 'CAT_KZ');


Insert into APP_PBE.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (7,  'ADM',  101, 3, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);
Insert into APP_PBE.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (8,  'BUD',  101, 3, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);
Insert into APP_PBE.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (9,  'ELE',  101, 3, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);
Insert into APP_PBE.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (10, 'MECH', 101, 3, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);
Insert into APP_PBE.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (11, 'ROL',  101, 3, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);
Insert into APP_PBE.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (12, 'TUR',  101, 3, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);
Insert into APP_PBE.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (13, 'MED',  101, 3, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);
Insert into APP_PBE.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (14, 'ART',  101, 3, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);



--2.  Szkolenia rozliczane 1h - max 2 bony

Insert into APP_PBE.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('SPD', 'Studia podyplomowe', 1, NULL, 'CAT_KZ');

Insert into APP_PBE.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (16, 'SPD',  101, 2, NULL, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);


--3.  Egzamin rozliczane max 80 bonów (1200 zł) EGZKZ

Insert into APP_PBE.TI_TRAINING_CATEGORIES  (ID, NAME, ORDINAL, PARENT_ID, CATALOG_ID) Values   ('EGZKZ', 'Egzamin', 1, NULL, 'CAT_KZ');

Insert into APP_PBE.TI_TRAINING_CATEGORY_PARAMS(ID, CATEGORY_ID, GRANT_PROGRAM_ID, PRODUCT_INSTANCE_FOR_HOUR, MAX_PRODUCT_INSTANCE, DATE_FROM, DATE_TO) Values (16, 'SPD',  101, NULL, 80, TO_DATE('2018-10-08', 'YYYY-MM-DD'), NULL);





--- Import kategorii katalogu: APP_PBE.TI_TRAINING_CATEGORIES
--- Katalog podpięty do programu: APP_PBE.TI_TRAINING_CATEGORY_CAT_GR_PR
--- APP_PBE.TI_TRAINING_CATEGORY_PARAMS ???



--Sparametryzowanie nowego programu:
--- dodanie programu APP_PBE.GRANT_PROGRAMS
--- dodanie produktu: APP_PBE.GRANT_OWNER_AID_PRODUCTS
--- dodanie produktu dla programu: APP_PBE.GRANT_PROGRAM_PRODUCTS
--- dodanie parametrów programu: APP_PBE.GRANT_PROGRAM_PARAMS
--- dodanie limitów: APP_PBE.GRANT_PROGRAM_LIMITS
--- dodanie czegoś: APP_PBE.GRANT_PROGRAM_ATTACH_REQ
--- przypięcie flow do kontraktu: APP_PBE.ORDER_FLOWS_FOR_GR_PROGRAMS (czy nowy flow?):

---- Definicja emisji bonów:
--- APP_PBE.PBE_PRODUCT_EMISSIONS
--- APP_PBE.PBE_PRODUCT_INSTANCES
---- generacja par:  APP_PBE.ACCOUNT_CONTRACT_PAIRS (CONTRACT_ID VS ACCOUNT_PAYMENT)

--- Definicja nowego flow:
----- APP_PBE.ORDER_FLOWS
----- APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS
----- APP_PBE.ORDER_FLOW_ALLOWED_DELAYS
/*PYTANIA:
- czy definicja nowego flow?
- czy definicja nowych uprawnień?
- czy import katalogu (app_pbe.ti_trainings) wymaga zmiany?
- jakie są importy?"*/


