
insert into  EAGLE.ADM_USERS (ID, STATUS, CREATED, MODIFIED) values ('SRV_EE_TI', 'A', '2016-12-31 23:59:59 EAGLE', '2016-12-31 23:59:59  EAGLE');
insert into  EAGLE.ADM_USERS (ID, STATUS, CREATED, MODIFIED) values ('SRV_EE_IND', 'A', '2016-12-31 23:59:59 EAGLE', '2016-12-31 23:59:59  EAGLE');


-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE_TI', 'ADM_PARAMETERS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE_TI', 'ZIP_CODES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE_TI', 'STATES', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE_TI', 'TI_TRA_INS_SEQ', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE_TI', 'PK_SEQ', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE_TI', 'PBE_E_REIMB_SEQ', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE_TI', 'ERMBS_ATTACH_SEQ', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE_TI', 'FILE_SEQ', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE_TI', 'CORREC_ATT_SEQ', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE_TI', 'PK_GRF_UTILS', 'EXECUTE'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TE_IND_USER_IN_ROLES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TE_PRIV_IN_ROLES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TE_PRIVILEGES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TE_ROLES', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'EMAIL_TEMPLATES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'EMAIL_INSTANCES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'EMAIL_INSTANCES', 'INSERT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'EMAIL_INSTANCE_ATTACHMENTS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'EMAIL_INSTANCE_ATTACHMENTS', 'INSERT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TI_TRAINING_CATEGORIES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TI_TRAINING_CATEGORY_CAT', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TI_TRAINING_CATEGORY_CAT_GR_PR', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TI_TRAINING_CATEGORY_PARAMS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TI_TRAINING_INSTANCE_STATUSES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TI_TRAINING_INSTANCES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TI_TRAINING_INSTANCES', 'INSERT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TI_TRAINING_INSTANCES', 'UPDATE'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TI_TRAININGS', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TI_USER_IN_ROLES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TI_USER_RESET_ATTEMPTS', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TRAINING_INSTITUTIONS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TRAINING_INSTITUTION_CONTACTS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TRAINING_INSTITUTION_USERS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'TRAINING_INSTITUTION_USERS', 'UPDATE'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'INDIVIDUAL_CONTACTS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'INDIVIDUAL_USERS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'INDIVIDUALS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'CONTACT_TYPES', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'EMPLOYMENTS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'ENTERPRISE_CONTACTS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'ENTERPRISES', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'CONTRACT_TYPES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'CONTRACT_TRAINING_CATEGORIES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'CONTRACTS', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCT_EMISSIONS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCT_INSTANCE_E', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCT_INSTANCE_E', 'INSERT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCT_INSTANCE_E_T', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCT_INSTANCE_POOL_E', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCT_INSTANCE_POOL_E', 'INSERT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCT_INSTANCE_POOL_E_T', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCT_INSTANCE_POOL_USES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCT_INSTANCE_POOL_USES', 'INSERT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCT_INSTANCE_POOL_USES', 'UPDATE'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCT_INSTANCE_POOLS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCT_INSTANCE_POOLS', 'INSERT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCT_INSTANCE_POOLS', 'UPDATE'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCT_INSTANCE_STATUSES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCT_INSTANCES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCT_INSTANCES', 'UPDATE'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'PBE_PRODUCTS', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'GRANT_PROGRAMS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'GRANT_OWNERS', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'ORDERS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'ORDER_FLOWS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'ORDER_FLOW_STATUSES', 'SELECT'));


  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'E_REIMBURSEMENTS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'E_REIMBURSEMENTS', 'INSERT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'E_REIMBURSEMENTS', 'UPDATE'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'E_REIMBURSEMENT_STATUSES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'E_REIMBURSEMENT_TYPES', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'E_REIMBURSEMENT_REPORTS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'E_REIMBURSEMENT_REPORTS', 'INSERT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'E_REIMBURSEMENT_EMAILS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'E_REIMBURSEMENT_EMAILS', 'INSERT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'E_REIMBURSEMENT_EMAILS', 'UPDATE'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'CORRECTIONS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'CORRECTIONS', 'INSERT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'CORRECTIONS', 'UPDATE'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'CORRECTION_ATTACHMENTS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'CORRECTION_ATTACHMENTS', 'INSERT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'CORRECTION_ATTACHMENTS', 'UPDATE'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'E_RMBS_ATTACHMENTS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'E_RMBS_ATTACHMENTS', 'INSERT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'E_RMBS_ATTACHMENTS', 'UPDATE'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'E_RMBS_ATTACHMENTS', 'DELETE'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'ATTACHMENT_TYPES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'ATTACHMENT_TYPES', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'ATTACHMENT_FILES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'ATTACHMENT_FILES', 'INSERT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'ATTACHMENT_FILES', 'UPDATE'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'GRANT_PROGRAM_ATTACH_REQ', 'SELECT'));

END;
/

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE_IND', 'ADM_PARAMETERS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE_IND', 'ZIP_CODES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE_IND', 'STATES', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('EAGLE', 'SRV_EE_IND', 'PK_SEQ', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'EMAIL_TEMPLATES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'EMAIL_INSTANCES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'EMAIL_INSTANCES', 'INSERT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'CONTACT_TYPES', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'INDIVIDUALS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'INDIVIDUAL_USERS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'INDIVIDUAL_USERS', 'UPDATE'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'INDIVIDUAL_CONTACTS', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'EMPLOYMENTS', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'TE_IND_USER_IN_ROLES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'TE_PRIV_IN_ROLES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'TE_PRIVILEGES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'TE_ROLES', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'CONTRACTS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'PBE_PRODUCT_INSTANCE_POOLS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'TI_TRAINING_INSTANCES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'ORDERS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'CONTRACT_TRAINING_CATEGORIES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'TI_TRAINING_CATEGORIES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'TI_TRAINING_CATEGORY_CAT', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'TI_TRAININGS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'TRAINING_INSTITUTIONS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'TI_TRAINING_INSTANCE_STATUSES', 'SELECT'));

  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'GRANT_PROGRAMS', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'GRANT_OWNERS', 'SELECT'));

END;
/
