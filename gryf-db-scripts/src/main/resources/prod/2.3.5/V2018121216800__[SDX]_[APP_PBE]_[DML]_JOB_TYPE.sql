--------------------------------------------TABELA JOB_TYPE --------------------------------------------
update ${gryf.schema}.JOB_TYPE SET job_name = 'asynchJobImportService'
 where GRANT_PROGRAM_ID IS NOT NULL AND name like '%IMPORT%' AND job_name != 'asynchJobImportService';
--------------------------------------------TABELA JOB_TYPE --------------------------------------------