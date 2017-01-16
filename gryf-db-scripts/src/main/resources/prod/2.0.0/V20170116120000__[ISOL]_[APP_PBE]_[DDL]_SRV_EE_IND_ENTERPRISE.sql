

-- uprawnienia
BEGIN
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'ENTERPRISES', 'SELECT'));
  dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE('APP_PBE', 'SRV_EE_IND', 'ENTERPRISE_CONTACTS', 'SELECT'));
END;
/
