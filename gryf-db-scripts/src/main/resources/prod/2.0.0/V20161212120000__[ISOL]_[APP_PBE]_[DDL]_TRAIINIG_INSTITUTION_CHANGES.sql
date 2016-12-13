alter table APP_PBE.TRAINING_INSTITUTIONS add EXTERNAL_ID VARCHAR2(20 BYTE);
COMMENT ON COLUMN APP_PBE.TRAINING_INSTITUTIONS.EXTERNAL_ID IS 'Zewn�trzy identyfikator instytucji szkoleniowej z BUR';
CREATE UNIQUE INDEX APP_PBE.TRA_INSIT_EXT_ID_UIDX ON APP_PBE.TRAINING_INSTITUTIONS (EXTERNAL_ID);
