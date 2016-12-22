-- ID na sztywno, ja bym ustawi³ sekwencje
Insert into APP_PBE.GRANT_PROGRAM_PARAM_TYPES (ID, NAME, DESCRIPRTION) values ('EX_POL_DAY','Liczba dni od wygaœniêcia','Parametr mówi¹cy ile dni powinno min¹æ od daty wygaœniêcia, by rozliczyæ pulê');
Insert into APP_PBE.GRANT_PROGRAM_PARAMS (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM, DATE_TO) values (5, 100, 'EX_POL_DAY', 10, null, null);