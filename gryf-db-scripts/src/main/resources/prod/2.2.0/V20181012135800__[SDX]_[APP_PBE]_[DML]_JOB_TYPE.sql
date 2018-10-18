--------------------------------------------TABELA IMPORT TYPE --------------------------------------------
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'IMPORT_CON', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKK' ), 'Import umów', 'asynchJobImportService', 'importContractService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'IMPORT_ORD', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKK' ), 'Import zamówień', 'asynchJobImportService', 'importOrderService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'IMPORT_TRA_INS', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKK' ), 'Import usługodawcy szkoleniowych', 'asynchJobImportService', 'importTrainingInstitutionService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'IMPORT_TRA', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKK' ),  'Import usług', 'asynchJobImportService', 'importTrainingService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'IMPORT_OPI', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKK' ), 'Import ocen', 'asynchJobImportService', 'importOpinionDoneService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'ORDER_TRANS', null, 'Zmiana statusu zamówienia', 'asynchJobOrderTransitionService', null);

INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'WZ_IMPORT_CON', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKZ' ), 'Import umów (WZ)', 'asynchJobImportService', 'importContractWZService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'WZ_IMPORT_ORD', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKZ' ), 'Import zamówień (WZ)', 'asynchJobImportService', 'importOrderWZService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'WZ_IMPORT_TRA_INS', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKZ' ), 'Import usługodawcy szkoleniowych (WZ)', 'asynchJobImportService', 'importTrainingInstitutionWZService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'WZ_IMPORT_TRA', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKZ' ),  'Import usług (WZ)', 'asynchJobImportService (WZ)', 'importTrainingWZService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'WZ_IMPORT_OPI', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKZ' ), 'Import ocen (WZ)', 'asynchJobImportService (WZ)', 'ImportOpinionDoneWZService');

--------------------------------------------------------------------------------------------------