
Insert into APP_PBE.GRANT_OWNERS (ID,NAME,EMAIL_ADDRESSES_GRANT_APP_INFO) values (100,'Sodexo - Kierunek Kariera','marcel.golunski@sodexo.com');
Insert into APP_PBE.GRANT_PROGRAMS (ID,GRANT_OWNER_ID,PROGRAM_NAME,START_DATE,END_DATE) values (100, 100,'Kierunek Kariera',to_date('16/10/01','RR/MM/DD'),null);
update APP_PBE.GRANT_PROGRAMS set PROGRAM_CODE = 'WUPKKK' where id = 100;

Insert into APP_PBE.GRANT_PROGRAM_PARAM_TYPES (ID, NAME, DESCRIPRTION) values ('OWN_CONT_P','Procent wk�adu w�asnego','Procent wk�adu w�asnego jak� u�ytkownik musi wp�aci� przy realizacji danego programu dofinansownaia. Warto��� parametru musi by� liczba bez znaku procent np: 13, 30.33, 12.341');
Insert into APP_PBE.GRANT_PROGRAM_PARAM_TYPES (ID, NAME, DESCRIPRTION) values ('SXO_NRB_RF','Rachunek bankowy Sxo do wyp�at','Rachunek bankowy Sodexo, z kt�rego wyp�acane s� �rodki w ramach programu');

Insert into APP_PBE.GRANT_PROGRAM_PARAMS (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM, DATE_TO) values (1, 100, 'OWN_CONT_P', '13', null, null);
Insert into APP_PBE.GRANT_PROGRAM_PARAMS (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM, DATE_TO) values (4, 100, 'SXO_NRB_RF', '32160010710003011205015002', null, null);

Insert into APP_PBE.GRANT_PROGRAM_LIMITS (ID, GRANT_PROGRAM_ID, ENTERPRISE_SIZE_ID, LIMIT_TYPE, LIMIT_VALUE, DATE_FROM, DATE_TO) values (8, 100, 'SELF', 'ORDVOULIM', 180, null, null);
Insert into APP_PBE.GRANT_PROGRAM_LIMITS (ID, GRANT_PROGRAM_ID, ENTERPRISE_SIZE_ID, LIMIT_TYPE, LIMIT_VALUE, DATE_FROM, DATE_TO) values (9, 100, 'SELF', 'ORDNUMLIM', 2, null, null);

Insert into APP_PBE.ORDER_FLOW_STATUSES (STATUS_ID,STATUS_NAME) values ('NEWKK', 'Nowe');
Insert into APP_PBE.ORDER_FLOW_STATUSES (STATUS_ID,STATUS_NAME) values ('PAIDKK', 'Op�acone');
Insert into APP_PBE.ORDER_FLOW_STATUSES (STATUS_ID,STATUS_NAME) values ('DOCGENKK', 'Wygenerowano dokumenty');
Insert into APP_PBE.ORDER_FLOW_STATUSES (STATUS_ID,STATUS_NAME) values ('PROCESSDKK', 'Zrealizowane');
Insert into APP_PBE.ORDER_FLOW_STATUSES (STATUS_ID,STATUS_NAME) values ('SENTKK', 'Wys�ane');
Insert into APP_PBE.ORDER_FLOW_STATUSES (STATUS_ID,STATUS_NAME) values ('EKSPORTKK', 'Wyeksportowane');
Insert into APP_PBE.ORDER_FLOW_STATUSES (STATUS_ID,STATUS_NAME) values ('CANCELLDKK', 'Anulowane');

Insert into APP_PBE.ORDER_FLOW_STATUS_PROPERTIES (ID, ORDER_FLOW_ID, STATUS_ID, FINAL_STATUS, SUCCESS_STATUS) values (4, 100, 'CANCELLDKK', 'Y', 'N');




Insert into APP_PBE.ORDER_FLOWS (ID,NAME,INITIAL_STATUS_ID) values (100,'Przepyw zam�wienia - Kierunek Kariera','NEWKK');
Insert into APP_PBE.ORDER_FLOWS_FOR_GR_PROGRAMS(ID, GRANT_PROGRAM_ID, ORDER_FLOW_ID, DATE_FROM, DATE_TO) values (1, 100, 100, null, null);


update APP_PBE.ORDER_FLOW_ALLOWED_DELAY_TYPES set NAME = 'Dopuszczalne op�nienie p�atno�ci po umowie', DESCRIPTION = 'Dopuszczalne op�nienie na dokonanie p�atno�ci po podpisaniu umowy' where ID = 'PAYAFSIGN';
Insert into APP_PBE.ORDER_FLOW_ALLOWED_DELAYS (ID,ORDER_FLOW_ID,DELAY_TYPE_ID,DELAY_VALUE,DELAY_DAYS_TYPE,REMARKS) values (9,100,'PAYAFSIGN',14,'B','Dozwolony czas na p�atno�� po podpisaniu umowy - 14 dni robczych');


Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS (ORDER_FLOW_ID,STATUS_ID,ACTION_ID,ACTION_NAME,ACTION_DESCRIPTION,ACTION_CLASS,NEXT_STATUS_ID,AUG_ID_REQUIRED, AUTOMATIC) values (100,'NEWKK',10012,'Rejestruj wp�at�','Akcja rejestruj�ca wpat� wkadu wasnego','careerDirectionRegisterPayment','PAIDKK','GRF_PBE_ORDERS_CONFIRM_PAYMENT', 'N');
Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS (ORDER_FLOW_ID,STATUS_ID,ACTION_ID,ACTION_NAME,ACTION_DESCRIPTION,ACTION_CLASS,NEXT_STATUS_ID,AUG_ID_REQUIRED, AUTOMATIC) values (100,'PAIDKK',10023,'Wydaj bony','Akcja wydaje bony elektroniczne','careerDirectionCreateProducyInstancePool','DOCGENKK','GRF_PBE_ORDERS_CREATE_PRODUCTS', 'Y');
Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS (ORDER_FLOW_ID,STATUS_ID,ACTION_ID,ACTION_NAME,ACTION_DESCRIPTION,ACTION_CLASS,NEXT_STATUS_ID,AUG_ID_REQUIRED, AUTOMATIC) values (100,'DOCGENKK',10034, 'Drukuj dokumenty','Akcja generuje niezb�dne dokumenty ksi�gowe','careerDirectionGenerateDocuments','PROCESSDKK','GRF_PBE_ORDERS_PRINT_DOC', 'Y');
Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS (ORDER_FLOW_ID,STATUS_ID,ACTION_ID,ACTION_NAME,ACTION_DESCRIPTION,ACTION_CLASS,NEXT_STATUS_ID,AUG_ID_REQUIRED, AUTOMATIC) values (100,'PROCESSDKK',10045,'Wy�lij zam�wienie','Akcja wysy�a maila do u�ytkownika z niezbednymi danymi','careerDirectionSendOrder','SENTKK','GRF_PBE_ORDERS_SEND', 'Y');

Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS (ORDER_FLOW_ID,STATUS_ID,ACTION_ID,ACTION_NAME,ACTION_DESCRIPTION,ACTION_CLASS,NEXT_STATUS_ID,AUG_ID_REQUIRED, AUTOMATIC) values (100,'NEWKK',10015,'Anuluj','Akcja anuluje zam�wienie',null,'CANCELLDKK','GRF_PBE_ORDERS_CANCEL', 'N');
Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS (ORDER_FLOW_ID,STATUS_ID,ACTION_ID,ACTION_NAME,ACTION_DESCRIPTION,ACTION_CLASS,NEXT_STATUS_ID,AUG_ID_REQUIRED, AUTOMATIC) values (100,'PAIDKK',10025,'Anuluj','Akcja anuluje zam�wienie',null,'CANCELLDKK','GRF_PBE_ORDERS_CANCEL', 'N');

Insert into APP_PBE.ORDER_FLOW_ELEMENT_TYPES (ID,DESCRIPTION,COMPONENT_NAME) values ('C_CONTRA','Element z�o�ony reprezentuj�cy podstawowe dane o umowie. Front: html i JS, Backend: serwisy, DTOsy do obs�ugi elementu z�o�onego powo�ywane s� dynamicznie na podstawie nazwy przechowywanej w kolumnie COMPONENT_NAME','ComplexTypeBasicContractInfo');
Insert into APP_PBE.ORDER_FLOW_ELEMENT_TYPES (ID,DESCRIPTION,COMPONENT_NAME) values ('C_PBEPRO','Element z�o�ony reprezentuj�cy podstawowe dane o bonach elektronicznych. Front: html i JS, Backend: serwisy, DTOsy do obs�ugi elementu z�o�onego powo�ywane s� dynamicznie na podstawie nazwy przechowywanej w kolumnie COMPONENT_NAME','ComplexTypePbeProductInfo');




Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('ORDINF_KK','Informacje o zam�wieniu','C_BSORDIFO',null,'Element z�o�ony przetrzymuj�cy podstawowe informacje o zam�wieniu');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('CONTRA_KK','Informacje o umowie','C_CONTRA',null,'Element z�o�ony przetrzymuj�cy podstawowe informacje o umowie');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('ADRINV_KK','Adres do faktury','ATTR_V','MAX_LENGTH=100','Element przetrzymuj�cy adres na fakturze');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('ADRCOR_KK','Adres korespondencyjny','ATTR_V','MAX_LENGTH=100','Element przetrzymuj�cy adres korespondencyjny');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('PCODINV_KK','Kod pocztowy do faktury','ATTR_V','MAX_LENGTH=100','Element przetrzymuj�cy kod pocztowy na fakturze');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('PCODCOR_KK','Kod pocztowy korespondencyjny','ATTR_V','MAX_LENGTH=100','Element przetrzymuj�cy kod pocztowy korespondencyjny');


Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('PBEPRO_KK','Informacje o bonach','C_PBEPRO',null,'Element z�o�ony przetrzymuj�cy podstawowe informacje o bonach elektronicznych');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('PRDNUM_KK','Ilo�� bon�w','ATTR_N','FORMAT=INTEGER;MIN_VALUE=0;MAX_VALUE=1000','Element przetrzynuj�cy ilo�� bon�w, kt�re b�d� realizowane w danym zamowieniu.');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('PRDAMO_KK','Kwota jednego bonu','ATTR_N','FORMAT=CURRENCY','Element przetrzynuj�cy kwot� jednego bonu.');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('OWNCONP_KK','Procent wk�adu w�asnego','ATTR_N','FORMAT=CURRENCY','Element przetrzynuj�cy procent wk�adu w�asnego, jaki u�ytkownik musi wp�aci� aby realizowac bony.');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('OWNCONA_KK','Kwota wk�adu w�asnego','ATTR_N','FORMAT=CURRENCY','Element przetrzynuj�cy kwot� wk�adu w�asnego, jaki u�ytkownik musi wp�aci� aby realizowac bony.');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('GRAAMO_KK','Kwota dofinansowania','ATTR_N','FORMAT=CURRENCY','Element przetrzynuj�cy kwot� dofinansowania');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('ORDAMO_KK','Kwota zam�wienia','ATTR_N','FORMAT=CURRENCY','Element przetrzynuj�cy kwot� zamowienia');

Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('PAYD_KK','Data wp�aty (wp�yni�cia �rodk�w)','ATTR_D','FORMAT=DATE','Data wp�aty (wp�yni�cia �rodk�w)');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('COND_KK','Data podpi�cia p�atno�ci','ATTR_D','FORMAT=DATE','Data podpi�cia p�atno�ci');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('PAYAMA_KK','Kwota wp�aty','ATTR_N','FORMAT=CURRENCY','Kwota wp�aty');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('CONAMA_KK','Kwota podpi�cia','ATTR_N','FORMAT=CURRENCY','Kwota podpi�cia');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('TITLE_KK','Tytu� przelewu','ATTR_V','MAX_LENGTH=50;TYPE=TEXTAREA;ROWS=10;COLS=50; ','Tytu� przelewu');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('DOWNDIS_KK','Nota obcia�eniowo-ksi�gowa na wk�ad w�asny','ATTACHMENT',null,'Element reprezentuj�cy wygenerowan� not� obcia�eniowo-ksi�gow� na wk�ad w�asny - pole uzupe�niane automatycznie');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('ATTAC01_KK','Nota obcia�eniowo-ksi�gowa na wk�ad w�asny (gdy inna ni� wygenerowana)','ATTACHMENT',null,'Element reprezentuj�cy not� obcia�eniowo-ksi�gow� na wk�ad w�asny wysylan� do u�ytkownika');

Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('ORDEMA_KK','Email do uczestnika o zrealizowaniu zam�wienia','C_EMAIL','EMAIL_SERVICE_BEAN=pbeProductOrderEmailService;ROWS=10;COLS=50','Element reprezentuj�cy pola emaila (z wy��czeniem za��cznik�w) z informacj� o zrealizownaiu zam�wieniu na bony elektroniczne');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS (ELEMENT_ID,ELEMENT_NAME,ELEMENT_TYPE_ID,ELEMENT_TYPE_PARAMS,ELEMENT_DESCRIPTION) values ('CANREAS_KK','Przyczyna anulowania zam�wienia','COMBOBOX','SQL_EXPRESSION=select ''BRAK_WP'', ''Nie wpacono wpaty wasnej'' from dual union select ''INNE'', ''Inne''  from dual','Przyczyna anulowania zam�wienia');





Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('NEWKK',       'ORDINF_KK',1, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PAIDKK',      'ORDINF_KK',1, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('DOCGENKK',    'ORDINF_KK',1, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'ORDINF_KK',1, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'ORDINF_KK',1, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'ORDINF_KK',1, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('CANCELLDKK',  'ORDINF_KK',1, null, null,null);

Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('NEWKK',       'CONTRA_KK',2, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PAIDKK',      'CONTRA_KK',2, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('DOCGENKK',    'CONTRA_KK',2, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'CONTRA_KK',2, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'CONTRA_KK',2, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'CONTRA_KK',2, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('CANCELLDKK',  'CONTRA_KK',2, null, null,null);

Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('NEWKK',       'ADRINV_KK',10, 'IUM', null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PAIDKK',      'ADRINV_KK',10, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('DOCGENKK',    'ADRINV_KK',10, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'ADRINV_KK',10, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'ADRINV_KK',10, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'ADRINV_KK',10, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('CANCELLDKK',  'ADRINV_KK',10, null, null,null);

Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('NEWKK',       'PCODINV_KK',11, 'IUM', null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PAIDKK',      'PCODINV_KK',11, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('DOCGENKK',    'PCODINV_KK',11, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'PCODINV_KK',11, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'PCODINV_KK',11, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'PCODINV_KK',11, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('CANCELLDKK',  'PCODINV_KK',11, null, null,null);


Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('NEWKK',       'ADRCOR_KK',12, 'IUM', null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PAIDKK',      'ADRCOR_KK',12, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('DOCGENKK',    'ADRCOR_KK',12, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'ADRCOR_KK',12, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'ADRCOR_KK',12, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'ADRCOR_KK',12, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('CANCELLDKK',  'ADRCOR_KK',12, null, null,null);

Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('NEWKK',       'PCODCOR_KK',13, 'IUM', null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PAIDKK',      'PCODCOR_KK',13, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('DOCGENKK',    'PCODCOR_KK',13, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'PCODCOR_KK',13, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'PCODCOR_KK',13, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'PCODCOR_KK',13, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('CANCELLDKK',  'PCODCOR_KK',13, null, null,null);


Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('NEWKK',       'PBEPRO_KK',20, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PAIDKK',      'PBEPRO_KK',20, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('DOCGENKK',    'PBEPRO_KK',20, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'PBEPRO_KK',20, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'PBEPRO_KK',20, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'PBEPRO_KK',20, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('CANCELLDKK',  'PBEPRO_KK',20, null, null,null);

Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('NEWKK',       'PAYD_KK',30, 'IUM', null,'PAYAFSIGN');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PAIDKK',      'PAYD_KK',30, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('DOCGENKK',    'PAYD_KK',30, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'PAYD_KK',30, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'PAYD_KK',30, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'PAYD_KK',30, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('CANCELLDKK',  'PAYD_KK',30, null, null,null);

Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('NEWKK',       'COND_KK',31, 'IUM', null,'PAYAFSIGN');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PAIDKK',      'COND_KK',31, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('DOCGENKK',    'COND_KK',31, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'COND_KK',31, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'COND_KK',31, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'COND_KK',31, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('CANCELLDKK',  'COND_KK',31, null, null,null);

Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('NEWKK',       'PAYAMA_KK',32, 'IUM', null,'PAYAFSIGN');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PAIDKK',      'PAYAMA_KK',32, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('DOCGENKK',    'PAYAMA_KK',32, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'PAYAMA_KK',32, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'PAYAMA_KK',32, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'PAYAMA_KK',32, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('CANCELLDKK',  'PAYAMA_KK',32, null, null,null);

Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('NEWKK',       'CONAMA_KK',33, 'IUM', null,'PAYAFSIGN');
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PAIDKK',      'CONAMA_KK',33, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('DOCGENKK',    'CONAMA_KK',33, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'CONAMA_KK',33, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'CONAMA_KK',33, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'CONAMA_KK',33, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('CANCELLDKK',  'CONAMA_KK',33, null, null,null);

Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('NEWKK',       'TITLE_KK',34, 'IU', null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PAIDKK',      'TITLE_KK',34, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('DOCGENKK',    'TITLE_KK',34, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'TITLE_KK',34, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'TITLE_KK',34, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'TITLE_KK',34, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('CANCELLDKK',  'TITLE_KK',34, null, null,null);

Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'DOWNDIS_KK',40, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'DOWNDIS_KK',40, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'DOWNDIS_KK',40, null, null,null);

Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'ORDEMA_KK',50, 'IUM', null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'ORDEMA_KK',50, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'ORDEMA_KK',50, null, null,null);

Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PROCESSDKK',  'ATTAC01_KK',60, 'IU', null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('SENTKK',      'ATTAC01_KK',60, null, null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('EKSPORTKK',   'ATTAC01_KK',60, null, null,null);




Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('NEWKK',       'CANREAS_KK',115, 'IU', null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('PAIDKK',      'CANREAS_KK',115, 'IU', null,null);
Insert into APP_PBE.ORDER_FLOW_ELEMENTS_IN_STATUS (STATUS_ID,ELEMENT_ID,POS,FLAGS,AUG_ID_REQUIRED,ALLOWED_DELAY_TYPE_ID) values ('CANCELLDKK',  'CANREAS_KK',115, null, null,null);





--Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS (ORDER_FLOW_ID,STATUS_ID,ACTION_ID,ACTION_NAME,ACTION_DESCRIPTION,ACTION_CLASS,NEXT_STATUS_ID,AUG_ID_REQUIRED) values (100,'NEWKK',10011,'TEST','TEST',null,'NEWKK',null);
--Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS (ORDER_FLOW_ID,STATUS_ID,ACTION_ID,ACTION_NAME,ACTION_DESCRIPTION,ACTION_CLASS,NEXT_STATUS_ID,AUG_ID_REQUIRED) values (100,'PAIDKK',10021,'TEST','TEST',null,'NEWKK',null);
--Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS (ORDER_FLOW_ID,STATUS_ID,ACTION_ID,ACTION_NAME,ACTION_DESCRIPTION,ACTION_CLASS,NEXT_STATUS_ID,AUG_ID_REQUIRED) values (100,'PROCESSDKK',10031,'TETS','TEST',null,'NEWKK',null);
--Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS (ORDER_FLOW_ID,STATUS_ID,ACTION_ID,ACTION_NAME,ACTION_DESCRIPTION,ACTION_CLASS,NEXT_STATUS_ID,AUG_ID_REQUIRED) values (100,'SENTKK',10031,'TEST','TEST',null,'NEWKK',null);
--Insert into APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS (ORDER_FLOW_ID,STATUS_ID,ACTION_ID,ACTION_NAME,ACTION_DESCRIPTION,ACTION_CLASS,NEXT_STATUS_ID,AUG_ID_REQUIRED) values (100,'CANCELLDKK',10041,'TEST','TEST',null,'NEWKK',null);



