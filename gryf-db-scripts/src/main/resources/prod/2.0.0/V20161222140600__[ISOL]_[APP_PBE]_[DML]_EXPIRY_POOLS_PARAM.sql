-- ID na sztywno, ja bym ustawi� sekwencje
Insert into APP_PBE.GRANT_PROGRAM_PARAM_TYPES (ID, NAME, DESCRIPRTION) values ('EX_POL_DAY','Liczba dni od wyga�ni�cia','Parametr m�wi�cy ile dni powinno min�� od daty wyga�ni�cia, by rozliczy� pul�');
Insert into APP_PBE.GRANT_PROGRAM_PARAMS (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM, DATE_TO) values (5, 100, 'EX_POL_DAY', 10, null, null);