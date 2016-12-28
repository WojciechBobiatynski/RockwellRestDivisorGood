update EAGLE.ADM_PARAMETERS
set value = 'C:\Users\Isolution\Desktop\GryfWorkspace\GryfFileRepo\'
WHERE NAME = 'GRYF_PATH_ATTACHMENTS';



update APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS
set AUTOMATIC = 'N'
where ACTION_ID in (10045, 10034, 10023);


update APP_PBE.GRANT_PROGRAM_PARAMS
set value = '20'
where GRANT_PROGRAM_ID = 100 and PARAM_ID = 'M_ORD_CON';


update APP_PBE.TI_TRAININGS
set ACTIVE = 'Y',
  DEACTIVATE_USER = null,
  DEACTIVATE_DATE = null,
  DEACTIVATE_JOB_ID = null;

