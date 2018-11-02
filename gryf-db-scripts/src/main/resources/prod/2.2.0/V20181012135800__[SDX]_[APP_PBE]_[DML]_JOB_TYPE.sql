--------------------------------------------TABELA IMPORT TYPE --------------------------------------------

INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'IMPORT_CON', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKK' ), 'Import umów', 'asynchJobImportService', 'importContractService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'IMPORT_ORD', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKK' ), 'Import zamówień', 'asynchJobImportService', 'importOrderService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'IMPORT_TRA_INS', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKK' ), 'Import usługodawcy szkoleniowych', 'asynchJobImportService', 'importTrainingInstitutionService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'IMPORT_TRA', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKK' ),  'Import usług', 'asynchJobImportService', 'importTrainingService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'IMPORT_OPI', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKK' ), 'Import ocen', 'asynchJobImportService', 'importOpinionDoneService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'ORDER_TRANS', null, 'Zmiana statusu zamówienia', 'asynchJobOrderTransitionService', null);

INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'WZ_IMPORT_CON', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKZ' ), 'Import umów (WZ)', 'asynchJobImportService', 'importContractService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'WZ_IMPORT_ORD', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKZ' ), 'Import zamówień (WZ)', 'asynchJobImportService', 'importOrderService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'WZ_IMPORT_TRA_INS', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKZ' ), 'Import usługodawcy szkoleniowych (WZ)', 'asynchJobImportService', 'importTrainingInstitutionService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'WZ_IMPORT_TRA', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKZ' ),  'Import usług (WZ)', 'asynchJobImportService', 'importTrainingService');
INSERT INTO  ${gryf.schema}.JOB_TYPE VALUES (JOB_TYPE_seq.nextval, 'WZ_IMPORT_OPI', (SELECT ID FROM ${gryf.schema}.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKZ' ), 'Import ocen (WZ)', 'asynchJobImportService', 'ImportOpinionDoneService');

--------------------------------------------------------------------------------------------------

/*MERGE INTO APP_PBE.JOB_TYPE  ug USING (
SELECT
      ID ,
     NAME,
     GRANT_PROGRAM_ID,
     LABEL,
     JOB_NAME ,
    SERVICE_NAME
 FROM
      APP_PBE.JOB_TYPE
 WHERE NAME ='IMPORT_CON'
) ins ON ( ug.NAME = ins.name )
WHEN MATCHED THEN UPDATE
SET ug.GRANT_PROGRAM_ID = ins.GRANT_PROGRAM_ID,
    ug.LABEL = ins.LABEL,
    ug.job_name = ins.job_name,
    ug.service_name = ins.service_name
WHEN NOT MATCHED THEN INSERT (
     ID ,
     NAME,
     GRANT_PROGRAM_ID,
     LABEL,
     JOB_NAME ,
    SERVICE_NAME )  VALUES (JOB_TYPE_seq.nextval, 'IMPORT_CON', (SELECT ID FROM APP_PBE.GRANT_PROGRAMS WHERE PROGRAM_CODE ='WUPKKK' ), 'Import umów', 'asynchJobImportService', 'importContractService');*/