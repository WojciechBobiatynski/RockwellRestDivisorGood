alter table APP_PBE.EMAIL_INSTANCES add  DELAY_TIMESTAMP TIMESTAMP(6);
COMMENT ON COLUMN APP_PBE.EMAIL_INSTANCES.DELAY_TIMESTAMP IS 'Data op??ninia maila - mail b?dzie wys?any po danej dacie';

alter table APP_PBE.EMAIL_TEMPLATES add  EMAIL_TYPE VARCHAR2(4 BYTE) DEFAULT 'text';
update APP_PBE.EMAIL_TEMPLATES set EMAIL_TYPE = 'text' where EMAIL_TYPE is null;
alter table APP_PBE.EMAIL_TEMPLATES MODIFY (EMAIL_TYPE not null);

COMMENT ON COLUMN APP_PBE.EMAIL_TEMPLATES.EMAIL_TYPE IS 'Typ maila: html text';