BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'E_REIMBURSEMENT_INVOICES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'GRANT_PROGRAM_PARAM_TYPES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_TI', 'GRANT_PROGRAM_PARAMS', 'SELECT'));
END;
/

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'GRANT_PROGRAM_PARAM_TYPES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'GRANT_PROGRAM_PARAMS', 'SELECT'));
END;
/