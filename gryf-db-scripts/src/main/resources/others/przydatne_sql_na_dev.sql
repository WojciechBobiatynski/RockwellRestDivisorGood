update EAGLE.ADM_PARAMETERS
set value = 'C:\Users\Isolution\Desktop\GryfWorkspace\GryfFileRepo\'
WHERE NAME = 'GRYF_PATH_ATTACHMENTS';



update APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS
set AUTOMATIC = 'N'
where ACTION_ID in (10045, 10034, 10023);


update APP_PBE.GRANT_PROGRAM_LIMITS
set LIMIT_VALUE = 20
where GRANT_PROGRAM_ID = 100 and LIMIT_TYPE = 'ORDNUMLIM';

delete
from APP_FIN.INVOICE_LINES
where INV_ID = 814 and (POS_NUM <> 1 or POS_TYPE <> 'S');


MERGE INTO EAGLE.ADM_PARAMETERS ug USING (
                                           SELECT
                                             'GRYF_TI_USER_URL' NAME,
                                             'http://localhost:8080/gryf-ti-fo/' VALUE,
                                             'URL aplikacji TI' DESCRIPTION
                                           FROM dual
                                         ) ins
ON (ug.NAME = ins.NAME)
WHEN MATCHED THEN
UPDATE SET ug.VALUE = ins.VALUE, ug.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED THEN INSERT
  (NAME,VALUE,DESCRIPTION)
VALUES (ins.NAME, ins.VALUE, ins.DESCRIPTION);



MERGE INTO EAGLE.ADM_PARAMETERS ug USING (
                                           SELECT
                                             'GRYF_IND_USER_URL' NAME,
                                             'http://localhost:8080/gryf-ind-fo/' VALUE,
                                             'URL aplikacji IND' DESCRIPTION
                                           FROM dual
                                         ) ins
ON (ug.NAME = ins.NAME)
WHEN MATCHED THEN
UPDATE SET ug.VALUE = ins.VALUE, ug.DESCRIPTION = ins.DESCRIPTION
WHEN NOT MATCHED THEN INSERT
  (NAME,VALUE,DESCRIPTION)
VALUES (ins.NAME, ins.VALUE, ins.DESCRIPTION);

update APP_PBE.TI_TRAININGS
set ACTIVE = 'Y',
  DEACTIVATE_USER = null,
  DEACTIVATE_DATE = null,
  DEACTIVATE_JOB_ID = null;

