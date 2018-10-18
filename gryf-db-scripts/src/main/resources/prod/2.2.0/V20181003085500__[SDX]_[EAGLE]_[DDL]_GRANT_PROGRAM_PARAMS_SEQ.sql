--------------------------------------------SEQUENCE--------------------------------------------

CREATE SEQUENCE ${eagle.schema}.GRANT_PROGRAM_PARAMS_SEQ
MINVALUE 1
MAXVALUE 9999999999999999999999999999
INCREMENT BY 1
START WITH 20
CACHE 20
NOORDER NOCYCLE;

-- uprawnienia
BEGIN
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE(upper('${eagle.schema}'), 'SRV_EE', 'GRANT_PROGRAM_PARAMS_SEQ', 'ALL'));
	dbms_output.put_line(SYS.PK_ADMIN.F_GRANT_TO_EAGLE(upper('${eagle.schema}'), 'DEVELOPER', 'GRANT_PROGRAM_PARAMS_SEQ', 'SELECT'));
END;
/

--------------------------------------------------------------------------------------------------
