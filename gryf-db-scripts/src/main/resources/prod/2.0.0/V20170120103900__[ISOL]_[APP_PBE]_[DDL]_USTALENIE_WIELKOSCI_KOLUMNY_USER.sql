ALTER TABLE APP_PBE.ASYNCH_JOBS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.IMPORT_DATA_ROWS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.IMPORT_DATA_ROW_ERRORS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.TRAINING_INSTITUTION_USERS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.TRAINING_INSTITUTION_USERS MODIFY (EMAIL VARCHAR2(100 BYTE)) MODIFY (LOGIN VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.INDIVIDUAL_USERS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.TI_USER_RESET_ATTEMPTS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.TI_TRAININGS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.EMPLOYMENTS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.CONTRACTS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.ORDER_INVOICES MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.ACCOUNT_CONTRACT_PAIRS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.TI_TRAINING_INSTANCES MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCE_POOLS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCE_POOL_USES MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCE_POOL_E MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCES MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.PBE_PRODUCT_INSTANCE_E MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.E_REIMBURSEMENTS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.CORRECTIONS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.ATTACHMENT_FILES MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.E_RMBS_ATTACHMENTS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.REPORT_INSTANCES MODIFY (CREATED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.CORRECTION_ATTACHMENTS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.E_REIMBURSEMENT_INVOICES MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.E_REIMBURSEMENT_REPORTS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));
ALTER TABLE APP_PBE.E_REIMBURSEMENT_EMAILS MODIFY (CREATED_USER VARCHAR2(100 BYTE)) MODIFY (MODIFIED_USER VARCHAR2(100 BYTE));