alter table APP_PBE.EMAIL_INSTANCES add  DELAY_TIMESTAMP TIMESTAMP(6);

COMMENT ON COLUMN APP_PBE.EMAIL_INSTANCES.DELAY_TIMESTAMP IS 'Data opóŸninia maila - mail bêdzie wys³any po danej dacie';
