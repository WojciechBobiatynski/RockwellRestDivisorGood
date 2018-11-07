Insert into ${gryf.schema}.GRANT_PROGRAM_PARAM_TYPES
   (ID, NAME, DESCRIPRTION)
 Values
   ('PROG_PHONE', 'Telefon BOK', 'Telefon do BOK podawany na e-mailu');


Insert into ${gryf.schema}.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkz}' ), 'PROG_PHONE', '22/346-75-15', NULL,
    NULL
    );

Insert into ${gryf.schema}.GRANT_PROGRAM_PARAMS
   (ID, GRANT_PROGRAM_ID, PARAM_ID, VALUE, DATE_FROM,
    DATE_TO)
 Values
   (${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ.nextval, (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='${program.code.wupkkk}' ), 'PROG_PHONE', '22/346-75-05', NULL,
    NULL
    );
