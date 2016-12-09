update EAGLE.ADM_PARAMETERS
set value = 'C:\Users\Isolution\Desktop\GryfWorkspace\GryfFileRepo\'
WHERE NAME = 'GRYF_PATH_ATTACHMENTS';



update APP_PBE.ORDER_FLOW_STATUS_TRANSITIONS
set AUTOMATIC = 'N'
where ACTION_ID in (10045, 10034, 10023);

