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


update APP_PBE.TI_TRAININGS
set ACTIVE = 'Y',
  DEACTIVATE_USER = null,
  DEACTIVATE_DATE = null,
  DEACTIVATE_JOB_ID = null;

