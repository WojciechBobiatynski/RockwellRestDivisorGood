ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCES MODIFY (MODIFIED_USER VARCHAR2(100) NOT NULL, MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL);
ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCE_EVENTS MODIFY (MODIFIED_USER VARCHAR2(100) NOT NULL, MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL);
ALTER TABLE APP_PBE.PBE_PRODUCT_EMISSIONS MODIFY (MODIFIED_USER VARCHAR2(100) NOT NULL, MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL);
ALTER TABLE APP_PBE.PBE_PRD_INST_STATUS_DICS MODIFY (MODIFIED_USER VARCHAR2(100) NOT NULL, MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL);
ALTER TABLE APP_PBE.PBE_PRD_INST_EVN_TYPE_DICS MODIFY (MODIFIED_USER VARCHAR2(100) NOT NULL, MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL);
ALTER TABLE APP_PBE.INDIVIDUAL_USERS MODIFY (MODIFIED_USER VARCHAR2(100) NOT NULL, MODIFIED_TIMESTAMP TIMESTAMP(6) NOT NULL);