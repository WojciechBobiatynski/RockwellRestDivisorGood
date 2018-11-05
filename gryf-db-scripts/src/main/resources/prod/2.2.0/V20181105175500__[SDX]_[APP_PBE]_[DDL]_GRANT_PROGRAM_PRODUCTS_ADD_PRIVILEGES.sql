-- uprawnienia
BEGIN
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE(UPPER('${gryf.schema}'), 'SRV_EE_TI', 'GRANT_PROGRAM_PRODUCTS', 'ALL'));
END;
/