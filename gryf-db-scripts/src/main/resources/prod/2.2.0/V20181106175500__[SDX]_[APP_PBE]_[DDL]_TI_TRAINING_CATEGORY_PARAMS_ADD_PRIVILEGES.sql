-- uprawnienia
BEGIN
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE(UPPER('${gryf.schema}'), 'SRV_EE_IND', 'TI_TRAINING_CATEGORY_PARAMS', 'SELECT'));
END;
/