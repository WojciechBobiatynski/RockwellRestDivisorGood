alter table APP_PBE.EMAIL_INSTANCES add  DELAY_TIMESTAMP TIMESTAMP(6);

COMMENT ON COLUMN APP_PBE.EMAIL_INSTANCES.DELAY_TIMESTAMP IS 'Data op�ninia maila - mail b�dzie wys�any po danej dacie';
