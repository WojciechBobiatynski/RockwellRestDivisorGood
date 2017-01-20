ALTER TABLE APP_PBE.EMAIL_TEMPLATES ADD EMAIL_BODY_HTML_TEMPLATE	CLOB;
COMMENT ON COLUMN APP_PBE.EMAIL_TEMPLATES.EMAIL_BODY_HTML_TEMPLATE IS 'Template treœci e-maila html, mo¿e zawieraæ placeholdery np: {$emailPlainBodyTemplates} - w teym miejscu zostnaie wklejony tekst z kolumny EMAIL_BODY_TEMPLATE';

alter table APP_PBE.EMAIL_INSTANCES add  EMAIL_TYPE VARCHAR2(4 BYTE) DEFAULT 'text';
update APP_PBE.EMAIL_INSTANCES set EMAIL_TYPE = 'text' where EMAIL_TYPE is null;
alter table APP_PBE.EMAIL_INSTANCES MODIFY (EMAIL_TYPE not null);