Insert into APP_PBE.GRANT_PROGRAM_PARAM_TYPES
   (ID, NAME, DESCRIPRTION)
 Values
   ('AC_PAY_PRT', 'Część numeru konta uczestnika', 'Struktura numeru konta APP_PBE.CONTRACTS.ACCOUNT_PAYMENT:
KK AAAA AAAA BBBB BBBB CCCN NNNN
- KK – dwie cyfry kontrolne
- AAAA AAAA – nr banku
- BBBB BBBB – numer konta (parametr ACCOUNT_PAYMENT_PRT)
- CCC - prefix kolejnego numeru konta (parametr CODE_PRFX)
- N NNNN - Kolejne numery dla KKZ
');


Insert into ${gryf.schema}.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'AC_PAY_PRT', '87261111', NULL,
    NULL
    );

Insert into ${gryf.schema}.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkk}' ), 'AC_PAY_PRT', '88941111', NULL,
    NULL
    );
